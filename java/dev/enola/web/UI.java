/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2023-2024 The Enola <https://enola.dev> Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.enola.web;

import static com.google.common.net.MediaType.HTML_UTF_8;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.ExtensionRegistryLite;

import dev.enola.common.convert.ConversionException;
import dev.enola.common.io.metadata.MetadataProvider;
import dev.enola.common.io.resource.ClasspathResource;
import dev.enola.common.io.resource.MemoryResource;
import dev.enola.common.io.resource.ReadableResource;
import dev.enola.common.io.resource.ReplacingResource;
import dev.enola.common.io.resource.StringResource;
import dev.enola.common.protobuf.ProtoIO;
import dev.enola.common.protobuf.ProtobufMediaTypes;
import dev.enola.common.protobuf.TypeRegistryWrapper;
import dev.enola.core.EnolaException;
import dev.enola.core.proto.EnolaServiceGrpc.EnolaServiceBlockingStub;
import dev.enola.core.proto.GetFileDescriptorSetRequest;
import dev.enola.core.proto.GetThingRequest;
import dev.enola.core.view.EnolaMessages;
import dev.enola.datatype.DatatypeRepository;
import dev.enola.datatype.DatatypeRepositoryBuilder;

import java.io.IOException;
import java.net.URI;

public class UI implements WebHandler {

    // TODO Use Appendable-based approach, for better memory efficiency, and less String "trashing"

    private static final ReadableResource HTML_FRAME =
            new ClasspathResource("templates/index.html");

    private final DatatypeRepository datatypeRepository =
            new DatatypeRepositoryBuilder().build(); // TODO look up in global repository!

    private final EnolaServiceBlockingStub service;
    private final TypeRegistryWrapper typeRegistryWrapper;
    private final EnolaMessages enolaMessages;
    private final EnolaThingProvider /* TODO ThingProvider*/ thingProvider;
    private final ThingUI thingUI;
    private ProtoIO protoIO;

    public UI(EnolaServiceBlockingStub service, MetadataProvider metadataProvider)
            throws DescriptorValidationException {
        this.service = service;
        var gfdsr = GetFileDescriptorSetRequest.newBuilder().build();
        var fds = service.getFileDescriptorSet(gfdsr).getProtos();
        typeRegistryWrapper = TypeRegistryWrapper.from(fds);
        var extensionRegistry = ExtensionRegistryLite.getEmptyRegistry();
        enolaMessages = new EnolaMessages(typeRegistryWrapper, extensionRegistry);
        thingProvider = new EnolaThingProvider(service);

        thingUI = new ThingUI(metadataProvider);
    }

    public void register(WebServer server) {
        server.register("/ui", this);
        server.register("/ui/static/", new StaticWebHandler("/ui/static/", "static"));
    }

    @Override
    public ListenableFuture<ReadableResource> get(URI uri) {
        try {
            String html = getHTML(uri);
            var resource = StringResource.of(html, HTML_UTF_8);
            return Futures.immediateFuture(resource);
        } catch (EnolaException | IOException | ConversionException e) {
            return Futures.immediateFailedFuture(e);
        }
    }

    private String getHTML(URI uri) throws EnolaException, IOException, ConversionException {
        var path = uri.getPath();
        if (path.startsWith("/ui/")) {
            var eri = path.substring("/ui/".length());
            return getEntityHTML(eri, false);
        } else {
            // TODO Create HTML page “frame” from template, with body from another template
            return new ClasspathResource("static/404.html").charSource().read();
        }
    }

    private String getEntityHTML(String iri, boolean usingNewAPI)
            throws EnolaException, IOException, ConversionException {
        var request = GetThingRequest.newBuilder().setIri(iri).build();
        var response = service.getThing(request);
        var any = response.getThing();
        var message = enolaMessages.toMessage(any);

        var thing = thingProvider.get(iri);
        var thingHTML = thingUI.html(thing).toString();

        // TODO Replace this with a *.yaml (et al.) link in the UI!
        var yamlResource = new MemoryResource(ProtobufMediaTypes.PROTOBUF_YAML_UTF_8);
        getProtoIO().write(message, yamlResource);
        var thingYAML = yamlResource.charSource().read();

        return new ReplacingResource(
                        HTML_FRAME, "%%ERI%%", iri, "%%THING%%", thingHTML, "%%YAML%%", thingYAML)
                .charSource()
                .read();
    }

    private ProtoIO getProtoIO() {
        if (protoIO == null) {
            protoIO = new ProtoIO(typeRegistryWrapper.get());
        }
        return protoIO;
    }
}

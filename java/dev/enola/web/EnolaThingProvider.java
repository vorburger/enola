/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2024 The Enola <https://enola.dev> Authors
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

import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.ExtensionRegistryLite;

import dev.enola.common.protobuf.TypeRegistryWrapper;
import dev.enola.core.entity.IDValueConverter;
import dev.enola.core.proto.EnolaServiceGrpc.EnolaServiceBlockingStub;
import dev.enola.core.proto.GetFileDescriptorSetRequest;
import dev.enola.core.proto.GetThingRequest;
import dev.enola.core.view.EnolaMessages;
import dev.enola.data.ProviderFromIRI;
import dev.enola.thing.message.MessageToThingConverter;
import dev.enola.thing.message.MessageWithIRI;
import dev.enola.thing.message.MoreThings;
import dev.enola.thing.proto.Thing;

import java.io.IOException;
import java.io.UncheckedIOException;

public class EnolaThingProvider
        implements ProviderFromIRI<Thing> /* TODO implements ProtoThingProvider */ {
    // TODO Move into dev.enola.core.thing where it probably belongs, more logically?
    // TODO Resolve (some) overlap this class has with abstract class ProtoToThingConnector
    // TODO Resolve (some) overlap this class has with abstract class ThingConnectorsProvider

    private final MessageToThingConverter m2t = new MessageToThingConverter(new IDValueConverter());
    private final EnolaServiceBlockingStub service;
    private final EnolaMessages enolaMessages;

    public EnolaThingProvider(EnolaServiceBlockingStub service)
            throws DescriptorValidationException {
        this.service = service;

        var gfdsr = GetFileDescriptorSetRequest.newBuilder().build();
        var fds = service.getFileDescriptorSet(gfdsr).getProtos();
        TypeRegistryWrapper typeRegistryWrapper = TypeRegistryWrapper.from(fds);
        var extensionRegistry = ExtensionRegistryLite.getEmptyRegistry();
        enolaMessages = new EnolaMessages(typeRegistryWrapper, extensionRegistry);
    }

    @Override
    public Thing get(String iri) {
        var request = GetThingRequest.newBuilder().setIri(iri).build();
        var response = service.getThing(request);

        if (!response.hasThing()) {
            return null;
        }
        var any = response.getThing();

        try {
            var things = MoreThings.fromAny(any);
            if (!things.isEmpty()) {
                // TODO This is wrong, we need to return Things here, and UI needs to show all?
                return things.getFirst();
            } else {
                var message = enolaMessages.toMessage(any);
                return m2t.convert(new MessageWithIRI(iri, message)).build();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

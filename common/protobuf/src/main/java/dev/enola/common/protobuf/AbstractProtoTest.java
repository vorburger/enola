/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2023 The Enola <https://enola.dev> Authors
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
package dev.enola.common.protobuf;

import static com.google.common.io.Resources.getResource;

import com.google.protobuf.Message;

import dev.enola.common.io.resource.ReadableResource;
import dev.enola.common.io.resource.UrlResource;

import org.junit.Test;

import java.io.IOException;

public abstract class AbstractProtoTest {
    private final ReadableResource resource;
    private final Message.Builder builder;

    public AbstractProtoTest(String pathToResourceOnClasspath, Message.Builder builder) {
        this.resource = classpath(pathToResourceOnClasspath);
        this.builder = builder;
    }

    @Test
    public void testProto() throws IOException {
        // TODO new ProtoIO().merge(resource, DynamicMessage.newBuilder(descriptor));
        new ProtoIO().read(resource, builder);
    }

    protected ReadableResource classpath(String pathToResourceOnClasspath) {
        return new UrlResource(getResource(pathToResourceOnClasspath));
    }
}

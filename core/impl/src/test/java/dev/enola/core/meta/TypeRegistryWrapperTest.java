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
package dev.enola.core.meta;

import static com.google.common.truth.Truth.assertThat;

import com.google.protobuf.Timestamp;

import org.junit.Test;

import java.util.List;

public class TypeRegistryWrapperTest {
    @Test
    public void empty() {
        assertThat(TypeRegistryWrapper.newBuilder().build().names()).isEmpty();
    }

    @Test
    public void one() {
        var wrapper =
                TypeRegistryWrapper.newBuilder().add(List.of(Timestamp.getDescriptor())).build();
        assertThat(wrapper.names()).containsExactly("google.protobuf.Timestamp");
    }
}
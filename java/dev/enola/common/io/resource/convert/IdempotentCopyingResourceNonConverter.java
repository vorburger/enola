/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2024-2025 The Enola <https://enola.dev> Authors
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
package dev.enola.common.io.resource.convert;

import dev.enola.common.io.resource.ReadableResource;
import dev.enola.common.io.resource.WritableResource;

import java.io.IOException;

/**
 * Copies all bytes from the input into the output resource.
 *
 * <p>This never actually "converts" anything!
 */
public class IdempotentCopyingResourceNonConverter implements CatchingResourceConverter {

    @Override
    public boolean convertIntoThrows(ReadableResource from, WritableResource into)
            throws IOException {
        from.byteSource().copyTo(into.byteSink());
        return true;
    }
}

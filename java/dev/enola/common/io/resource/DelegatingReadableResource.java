/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2023-2025 The Enola <https://enola.dev> Authors
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
package dev.enola.common.io.resource;

import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;

public abstract class DelegatingReadableResource extends BaseResource implements ReadableResource {

    protected final ReadableResource delegate;

    protected DelegatingReadableResource(ReadableResource delegate) {
        super(delegate.uri(), delegate.mediaType());
        this.delegate = delegate;
    }

    @Override
    public ByteSource byteSource() {
        return delegate.byteSource();
    }

    @Override
    public CharSource charSource() {
        return delegate.charSource();
    }
}

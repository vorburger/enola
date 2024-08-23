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

import static com.google.common.util.concurrent.Futures.immediateFuture;

import com.google.common.util.concurrent.ListenableFuture;

import dev.enola.common.io.resource.ClasspathResource;
import dev.enola.common.io.resource.ReadableResource;

import java.net.URI;

/**
 * {@link WebHandler} which servers static web content from the Java classpath. Useful e.g. for
 * fixed CSS + JS files, or https://www.webjars.org.
 */
public class StaticWebHandler implements WebHandler {

    // TODO Rename StaticWebHandler to dev.enola.common.net.http.HttpClasspathResourceHandler

    private final String uriPrefix;
    private final String classpathPrefix;

    public StaticWebHandler(String uriPrefix, String classpathPrefix) {
        this.uriPrefix = uriPrefix;
        this.classpathPrefix = classpathPrefix;
    }

    @Override
    public ListenableFuture<ReadableResource> get(URI uri) {
        var path = uri.getPath();
        if (path.contains("..")) {
            throw new IllegalArgumentException("URI cannot contain '..':" + path);
        }
        if (!path.startsWith(uriPrefix)) {
            throw new IllegalStateException(path + " does not start with " + uriPrefix);
        }
        var cutPath = path.substring(uriPrefix.length());
        var resource = new ClasspathResource(classpathPrefix + "/" + cutPath);
        return immediateFuture(resource);
    }
}

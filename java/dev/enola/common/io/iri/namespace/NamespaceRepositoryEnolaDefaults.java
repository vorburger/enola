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
package dev.enola.common.io.iri.namespace;

public class NamespaceRepositoryEnolaDefaults {

    // TODO Replace this with something which reads e.g. //models/enola.dev/namespaces.ttl

    public static final NamespaceRepository INSTANCE =
            new NamespaceRepositoryBuilder()
                    .store("enola", "https://enola.dev/")
                    .store("xsd", "http://www.w3.org/2001/XMLSchema#")
                    .store("schema", "https://schema.org/")
                    .store("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
                    .store("rdfs", "http://www.w3.org/2000/01/rdf-schema#")
                    .store("foaf", "http://xmlns.com/foaf/0.1/")
                    .store("dc", "http://purl.org/dc/elements/1.1/")
                    .store("owl", "http://www.w3.org/2002/07/owl#")
                    .store("ex", "https://example.org/")
                    .build();
}

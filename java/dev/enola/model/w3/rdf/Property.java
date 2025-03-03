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
package dev.enola.model.w3.rdf;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

import dev.enola.model.w3.rdfs.Class;
import dev.enola.model.w3.rdfs.Resource;
import dev.enola.thing.HasPredicateIRI;
import dev.enola.thing.KIRI;
import dev.enola.thing.Link;
import dev.enola.thing.impl.ImmutableThing;
import dev.enola.thing.java.ProxyTBF;

import java.util.Optional;

public interface Property extends Resource, HasPredicateIRI {

    default Optional<Property> subPropertyOf() {
        return getThing("http://www.w3.org/2000/01/rdf-schema#subPropertyOf", Property.class);
    }

    default Optional<Class> domain() {
        return getThing(KIRI.RDFS.DOMAIN, Class.class);
    }

    default Optional<Class> range() {
        return getThing("http://www.w3.org/2000/01/rdf-schema#range", Class.class);
    }

    @Override
    Builder<? extends Property> copy();

    // skipcq: JAVA-E0169
    interface Builder<B extends Property> extends Resource.Builder<B> {

        @CanIgnoreReturnValue
        default Builder<B> domain(String iri) {
            set(KIRI.RDFS.DOMAIN, new Link(iri));
            return this;
        }

        @Override
        @CanIgnoreReturnValue
        Builder<B> iri(String iri);
    }

    @SuppressWarnings("unchecked")
    static Property.Builder<Property> builder() {
        var builder =
                new ProxyTBF(ImmutableThing.FACTORY).create(Property.Builder.class, Property.class);
        builder.addType(KIRI.RDF.PROPERTY);
        return builder;
    }
}

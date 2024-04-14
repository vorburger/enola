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
package dev.enola.thing.gen.markdown;

import dev.enola.common.io.resource.ResourceProvider;
import dev.enola.thing.gen.Relativizer;
import dev.enola.thing.proto.Thing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

public class MarkdownSiteGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(MarkdownSiteGenerator.class);

    private final URI base;
    private final ResourceProvider rp;
    private final MarkdownThingGenerator mtg;

    public MarkdownSiteGenerator(URI base, ResourceProvider rp) {
        this.base = base;
        this.mtg = new MarkdownThingGenerator();
        this.rp = rp;
    }

    public void generate(Iterable<Thing> things) throws IOException {
        // TODO Do this multi-threaded, in parallel...
        for (var thing : things) {
            LOG.debug("Thing {}", thing);
            var thingIRI = thing.getIri();
            var relativeThingIRI = Relativizer.relativize(URI.create(thingIRI), "md");
            var outputIRI = base.resolve(relativeThingIRI);
            LOG.info("Generating {}", outputIRI);
            var outputResource = rp.getWritableResource(outputIRI);
            try (var writer = outputResource.charSink().openBufferedStream()) {
                mtg.generate(thing, writer, outputIRI, base);
            }
        }
    }

    // TODO Add MarkdownDocGenerator.FOOTER ;-)
}

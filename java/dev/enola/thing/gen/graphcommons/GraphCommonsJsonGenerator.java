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
package dev.enola.thing.gen.graphcommons;

import com.google.common.io.CharStreams;
import com.google.gson.FormattingStyle;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonWriter;

import dev.enola.common.context.TLC;
import dev.enola.common.convert.ConversionException;
import dev.enola.thing.Thing;
import dev.enola.thing.gen.ThingsIntoAppendableConverter;
import dev.enola.thing.repo.StackedThingProvider;
import dev.enola.thing.repo.ThingProvider;

import java.io.IOException;

/** Generator of JSON Format used by <a href="https://graphcommons.com/>Graph Commons</a>. */
public class GraphCommonsJsonGenerator implements ThingsIntoAppendableConverter {

    @Override
    public boolean convertInto(Iterable<Thing> from, Appendable out)
            throws ConversionException, IOException {
        var writer = CharStreams.asWriter(out);
        var jsonWriter = new JsonWriter(writer);
        jsonWriter.setStrictness(Strictness.STRICT);
        jsonWriter.setFormattingStyle(FormattingStyle.PRETTY); // TODO COMPACT, if ?pretty
        jsonWriter.setSerializeNulls(false);
        jsonWriter.setHtmlSafe(true); // TODO ?
        try (var ctx = TLC.open()) {
            ctx.push(ThingProvider.class, new StackedThingProvider(from));
            out.append("{\n \"nodes\": [\n");
            for (Thing thing : from) printThingNode(thing, jsonWriter);
            out.append(" ],\n \"edges\": [\n");
            for (Thing thing : from) printThingEdges(thing, jsonWriter);
            out.append(" ],\n \"nodeTypes\": [\n");
            // TODO printThingNodeTypes()
            out.append(" ],\n \"edgeTypes\": [\n");
            // TODO printThingEdgeTypes()
            out.append(" ],\n");
            out.append(" \"name\": \"Enola.dev\"\n");
            out.append('}');
        }
        jsonWriter.flush();
        writer.close();
        return true;
    }

    private void printThingNode(Thing thing, JsonWriter out) throws IOException {
        out.beginObject();
        out.name("id").value(thing.iri());
        out.endObject();
    }

    private void printThingEdges(Thing thing, JsonWriter out) {}
}
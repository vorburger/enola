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
package dev.enola.core.thing;

import com.google.protobuf.Any;

import dev.enola.core.EnolaException;
import dev.enola.core.proto.ListEntitiesRequest;
import dev.enola.core.proto.ListEntitiesResponse;

import java.util.Map;

public interface ThingService {
    // TODO Replace usages with ProtoThingProvider, once listEntities() is gone

    // TODO Replace Any with Thing, when old Entity is removed?
    // TODO throws ConversionException, only?
    Any getThing(String iri, Map<String, String> parameters); // TODO rm throws EnolaException;

    // TODO Replace all listEntities() callers with getThing() which returns Things
    ListEntitiesResponse listEntities(ListEntitiesRequest r) throws EnolaException;
}

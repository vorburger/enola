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
package dev.enola.thing.java;

import com.google.common.reflect.Reflection;

import dev.enola.thing.Thing;
import dev.enola.thing.impl.IImmutableThing;
import dev.enola.thing.impl.ImmutableThing;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ProxyTBF implements TBF {

    private final TBF wrap;

    /**
     * Constructor.
     *
     * @param wrap is a {@link TBF} such as {@link ImmutableThing#FACTORY} or {@link
     *     dev.enola.thing.impl.MutableThing#FACTORY}.
     */
    public ProxyTBF(TBF wrap) {
        this.wrap = wrap;
    }

    @Override
    public <B extends Thing.Builder<?>> boolean handles(Class<B> builderInterface) {
        return !wrap.handles(builderInterface);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Thing, B extends Thing.Builder<T>> B create(
            Class<B> builderInterface, Class<T> thingInterface) {
        return (B)
                create(
                        wrap,
                        -1,
                        (Class<Thing.Builder<IImmutableThing>>) builderInterface,
                        (Class<IImmutableThing>) thingInterface);
    }

    @SuppressWarnings("unchecked")
    private static Thing.Builder<? extends IImmutableThing> create(
            TBF tbf,
            int expectedSize,
            Class<Thing.Builder<IImmutableThing>> builderInterface,
            Class<IImmutableThing> thingInterface) {
        if (builderInterface.equals(Thing.Builder.class)) return create(tbf, expectedSize);

        var wrappedBuilder = // an ImmutableThing.Builder or a MutableThing (but NOT another Proxy)
                (Thing.Builder<? extends IImmutableThing>) create(tbf, expectedSize);
        var handler =
                new BuilderInvocationHandler(tbf, wrappedBuilder, builderInterface, thingInterface);
        var proxy = Reflection.newProxy(builderInterface, handler);
        if (!(builderInterface.isInstance(proxy)))
            throw new IllegalArgumentException(proxy.toString());
        return proxy;
    }

    private static Thing.Builder<? extends IImmutableThing> create(TBF tbf, int expectedSize) {
        if (expectedSize == -1) return tbf.create();
        else return tbf.create(expectedSize);
    }

    // TODO Consider using Guava's AbstractInvocationHandler?

    private record BuilderInvocationHandler(
            TBF tbf,
            Thing.Builder<? extends IImmutableThing> immutableThingBuilder,
            Class<Thing.Builder<IImmutableThing>> builderInterface,
            Class<IImmutableThing> thingInterface)
            implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.isDefault()) return InvocationHandler.invokeDefault(proxy, method, args);
            if (!method.getName().equals("build"))
                try {
                    return method.invoke(immutableThingBuilder, args);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                            "proxy="
                                    + proxy
                                    + "; method="
                                    + method
                                    + "; args="
                                    + Arrays.toString(args),
                            e);
                }
            var built = immutableThingBuilder.build();
            var handler = new ThingInvocationHandler(tbf, built, builderInterface, thingInterface);
            return Reflection.newProxy(thingInterface, handler);
        }
    }

    private record ThingInvocationHandler(
            TBF tbf,
            IImmutableThing thing,
            Class<Thing.Builder<IImmutableThing>> builderInterface,
            Class<IImmutableThing> thingInterface)
            implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.isDefault()) return InvocationHandler.invokeDefault(proxy, method, args);
            if (method.getName().equals("copy")) return copy();
            return method.invoke(thing, args);
        }

        private Thing.Builder<? extends IImmutableThing> copy() {
            var size = thing.properties().size();
            var builder = create(tbf, size, builderInterface, thingInterface);
            builder.iri(thing.iri());
            thing.properties()
                    .forEach(
                            (predicateIRI, object) -> {
                                var datatypeIRI = thing.datatype(predicateIRI);
                                builder.set(predicateIRI, object, datatypeIRI);
                            });
            return builder;
        }
    }
}

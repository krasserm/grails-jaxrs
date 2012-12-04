/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.jaxrs.support;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;

/**
 * Utility class related for {@link MessageBodyReaderSupport} and
 * {@link MessageBodyWriterSupport}
 *
 * @author Martin Krasser
 */
public class ProviderUtils {

    /**
     * Returns the type argument for {@link MessageBodyReaderSupport} defined by
     * a subclass.
     *
     * @param <T>
     *            {@link MessageBodyReaderSupport} subclass.
     * @param provider
     *            instance of {@link MessageBodyReaderSupport} subclass.
     * @return type argument for {@link MessageBodyReaderSupport} defined by a
     *         subclass.
     */
    public static <T extends MessageBodyReaderSupport<?>> Class getReaderTypeArgument(T provider) {
        return getTypeArgument(provider.getClass());
    }

    /**
     * Returns the type argument for {@link MessageBodyWriterSupport} defined by
     * a subclass.
     *
     * @param <T>
     *            {@link MessageBodyWriterSupport} subclass.
     * @param provider
     *            instance of {@link MessageBodyWriterSupport} subclass.
     * @return type argument for {@link MessageBodyWriterSupport} defined by a
     *         subclass.
     */
    public static <T extends MessageBodyWriterSupport<?>> Class getWriterTypeArgument(T provider) {
        return getTypeArgument(provider.getClass());
    }

    /**
     * Checks <code>mediaType</code> for XML compatibility.
     *
     * @param mediaType
     * @return <code>true</code> if <code>mediaType</code> is compatible with
     *         either <code>text/xml</code> or <code>application/xml</code>.
     */
    public static boolean isXmlType(MediaType mediaType) {
        return
            mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE) ||
            mediaType.isCompatible(MediaType.TEXT_XML_TYPE);
    }

    /**
     * Checks <code>mediaType</code> for JSON compatibility.
     *
     * @param mediaType
     * @return <code>true</code> if <code>mediaType</code> is compatible with
     *         either <code>text/x-json</code> or <code>application/json</code>.
     */
    public static boolean isJsonType(MediaType mediaType) {
        return
            mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE) ||
            mediaType.isCompatible(new MediaType("text", "x-json"));
    }

    private static Class getTypeArgument(Class<?> provider) {
        Class<?> clazz = provider;
        Type type = null;
        while (true) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType)t;
                type = pt.getActualTypeArguments()[0];
                break;
            }
            clazz = clazz.getSuperclass();
            // Should never end here
            if (clazz == null) {
                return null;
            }
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedTypeArg = (ParameterizedType)type;
            type = parameterizedTypeArg.getRawType();
        }
        if (!Class.class.isInstance(type)) {
            throw new IllegalTypeException("Unsupported type argument: " + type + ". Must be a class or interface.");
        }
        return (Class)type;
    }
}

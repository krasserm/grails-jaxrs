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

/**
 * Utility class related for {@link MessageBodyReaderSupport} and
 * {@link MessageBodyWriterSupport}
 * 
 * @author Martin Krasser
 */
class ProviderUtils {

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
    public static <T extends MessageBodyReaderSupport<?>> Type getReaderTypeArgument(T provider) {
        return getDeclaredProvidedType(provider.getClass());
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
    public static <T extends MessageBodyWriterSupport<?>> Type getWriterTypeArgument(T provider) {
        return getDeclaredProvidedType(provider.getClass());
    }
    
    private static Type getDeclaredProvidedType(Class<?> provider) {
        Class<?> clazz = provider;
        while (true) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType)t;
                return pt.getActualTypeArguments()[0];
            }
            clazz = clazz.getSuperclass();
            // Should never end here
            if (clazz == null) {
                return null;
            }
        }
    }
}

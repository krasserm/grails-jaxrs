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
 * @author Martin Krasser
 */
class ProviderUtils {

    public static <T extends MessageBodyReaderSupport<?>> Type getDeclaredReadingType(T provider) {
        return getDeclaredProvidedType(provider.getClass());
    }
    
    public static <T extends MessageBodyWriterSupport<?>> Type getDeclaredWritingType(T provider) {
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

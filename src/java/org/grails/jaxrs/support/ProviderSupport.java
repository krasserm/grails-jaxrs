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

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;

/**
 * @author Martin Krasser
 */
public abstract class ProviderSupport {

    private Class<?> typeArgument;

    public Class<?> getTypeArgument() {
        return typeArgument;
    }

    public void setTypeArgument(Class<?> typeArgument) {
        this.typeArgument = typeArgument;
    }

    /**
     * Returns <code>true</code> if {#link {@link #getTypeArgument()} is
     * assignable from <code>type</code>.
     */
    public boolean isSupported(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return typeArgument.isAssignableFrom(type);
    }
}

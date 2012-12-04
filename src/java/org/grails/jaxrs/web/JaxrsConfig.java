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
package org.grails.jaxrs.web;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * Defines the components (resource and provider classes) of a JAX-RS
 * applications.
 *
 * @author Martin Krasser
 */
public class JaxrsConfig extends Application {

    private Set<Class<?>> jaxrsClasses = Collections.synchronizedSet(new HashSet<Class<?>>());

    /**
     * Returns the resource and provider classes defined by a Grails JAX-RS
     * applications. The plugin will modify this set whenever resource or
     * provider code changes are made in development mode.
     *
     * @return a mutable, synchronized set of resource and provider classes.
     */
    @Override
    public Set<Class<?>> getClasses() {
        return jaxrsClasses;
    }

    /**
     * Resets this configuration by removing a classes from the set returned by
     * {@link #getClasses()}
     */
    public void reset() {
        jaxrsClasses.clear();
    }
}

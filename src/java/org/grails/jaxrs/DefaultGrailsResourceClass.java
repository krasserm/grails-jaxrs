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
package org.grails.jaxrs;

import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;

/**
 * @author Martin Krasser
 */
public class DefaultGrailsResourceClass extends AbstractInjectableGrailsClass implements GrailsResourceClass {

    public static final String RESOURCE = "Resource";

    public DefaultGrailsResourceClass(Class clazz) {
        super(clazz, RESOURCE);
    }

    public DefaultGrailsResourceClass(Class clazz, String trailingName) {
        super(clazz, trailingName);
    }
}

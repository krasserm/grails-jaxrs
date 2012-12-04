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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;

/**
 * @author Martin Krasser
 */
public class ResourceArtefactHandler extends ArtefactHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(ResourceArtefactHandler.class);

    public static final String TYPE = "Resource";

    public ResourceArtefactHandler() {
        super(TYPE, GrailsResourceClass.class, DefaultGrailsResourceClass.class, TYPE);
    }

    /**
     * Returns <code>true</code> if the <code>clazz</code> contains at least one
     * JAX-RS annotation on class-level or method-level and none of the provider
     * interfaces is implemented.
     *
     * @param clazz
     * @return <code>true</code> if the class is a JAX-RS resource.
     */
    @Override
    public boolean isArtefactClass(Class clazz) {
        if (clazz == null) {
            return false;
        }
        boolean match = JaxrsClasses.isJaxrsResource(clazz);
        if (match) {
            LOG.info("Detected JAX-RS resource: " + clazz.getName());
        }
        return match;
    }
}

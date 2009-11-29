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

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;

/**
 * @author Martin Krasser
 */
public class ProviderArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = "Provider";
    
    public ProviderArtefactHandler() {
        super(TYPE, GrailsProviderClass.class, DefaultGrailsProviderClass.class, TYPE);
    }

    /**
     * Returns <code>true</code> if the <code>clazz</code> either implements
     * {@link MessageBodyReader} or {@link MessageBodyWriter}.
     * 
     * @param clazz
     * @return <code>true</code> if the class is a JAX-RS provider.
     */
    @Override
    public boolean isArtefactClass(Class clazz) {
        if (clazz == null) {
            return false;
        }
        
        // TODO: implement according to JSR 311 specification
        
        return 
            (MessageBodyReader.class.isAssignableFrom(clazz)) ||
            (MessageBodyWriter.class.isAssignableFrom(clazz));
    }

}

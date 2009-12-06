import javax.ws.rs.core.MediaTypeimport java.lang.annotation.Annotationimport java.lang.reflect.Type/*
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

import javax.ws.rs.Produces
import javax.ws.rs.ext.Provider

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import org.grails.jaxrs.support.DomainObjectWriterSupport

/**
 * @author Martin Krasser
 */
@Provider
@Produces(['text/xml', 'application/xml', 'text/x-json', 'application/json'])
class TestWriter extends DomainObjectWriterSupport {

    TestWriter() {        ConfigurationHolder.config.org.grails.jaxrs.dowriter.disable = true    }
    
    boolean isWriteable(Class type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        type.equals(Person.class) && super.isWriteable(type, genericType, annotations, mediaType)
    }
    
    protected Object writeToXml(Object t, OutputStream entityStream, String charset) {        println '------------------------> Custom write'        super.writeToXml(t, entityStream, charset)    }    }

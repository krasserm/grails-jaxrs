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
package org.grails.jaxrs.provider

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.io.OutputStreamWriterimport javax.ws.rs.Producesimport grails.converters.JSON
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider

import grails.converters.XML

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware

/**
 * Very experimental provider to convert from Grails domain objects to XML 
 * streams. 
 * 
 * @author Martin Krasser
 */
@Provider
@Produces(['text/xml', 'application/xml', 'text/x-json', 'application/json'])
class DomainObjectWriter implements MessageBodyWriter<Object>, GrailsApplicationAware {

    GrailsApplication grailsApplication;
    
    long getSize(Object t, Class type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        -1
    }

    boolean isWriteable(Class type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        grailsApplication.isDomainClass(type) && (xmlType(mediaType) || jsonType(mediaType))
    }

    void writeTo(Object t, Class type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap httpHeaders, OutputStream entityStream) 
            throws IOException, WebApplicationException {
        def writer = new OutputStreamWriter(entityStream)
        def converter = null
        if (xmlType(mediaType)) {
            converter = new XML(t)
            converter.render(writer)
            writer.flush()
        } else {
            converter = new JSON(t)
            converter.render(writer)
        }
    }
 
    private static def xmlType = { mediaType ->
        mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE) ||
        mediaType.isCompatible(MediaType.TEXT_XML_TYPE)
    }
    
    private static def jsonType = { mediaType ->
        mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE) ||
        mediaType.isCompatible(new MediaType('text', 'x-json'))
    }

}

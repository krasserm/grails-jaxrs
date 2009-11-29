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

import static org.grails.jaxrs.provider.ConverterUtils.getDefaultEncoding
import static org.grails.jaxrs.provider.ConverterUtils.xmlToMap
import static org.grails.jaxrs.provider.ProviderUtils.*

import java.io.IOException
import java.io.InputStream
import java.lang.annotation.Annotation
import java.lang.reflect.Type

import javax.ws.rs.Consumes
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyReader
import javax.ws.rs.ext.Provider

import grails.converters.JSON

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware

/**
 * A message body reader that converts an XML or JSON entity streams to a domain
 * object. The request content type must be specified by the 
 * <code>Content-Type</code> HTTP header. Supported content types by this 
 * provider are <code>text/xml</code>, <code>application/xml</code>, 
 * <code>text/x-json</code> or <code>application/json</code>. Assuming 
 * <code>Note</code> is a Grails domain class. This provider supports usage of 
 * resource methods such as:
 * 
 * <pre>
 * &#064;Path('/notes')
 * class NotesResource {
 * 
 *      &#064;POST
 *      &#064;Consumes(['application/xml','application/json'])
 *      Response addNote(Note note) {
 *          note.save()
 *          // ...
 *      }
 *      
 * }
 * 
 * 
 * </pre>
 * 
 * This provider can be disabled by setting 
 * <code>org.grails.jaxrs.doreader.disable</code> to <code>true</code> in the 
 * application config.
 * 
 * @author Martin Krasser
 */
@Provider
@Consumes(['text/xml', 'application/xml', 'text/x-json', 'application/json'])
class DomainObjectReader implements MessageBodyReader<Object>, GrailsApplicationAware {

    GrailsApplication grailsApplication
    
    /**
     * Returns <code>true</code> if <code>type</code> is a Grails domain class 
     * and the request content type is one of <code>text/xml</code>, 
     * <code>application/xml</code>, <code>text/x-json</code> or 
     * <code>application/json</code>.
     */
    boolean isReadable(Class type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return grailsApplication.isDomainClass(type) && (isXmlType(mediaType) || isJsonType(mediaType))
    }

     /**
      * Creates a Grails domain object from an XML or JSON request entity 
      * stream.
      */
    Object readFrom(Class type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        
        if (ConfigurationHolder.config.org.grails.jaxrs.doreader.disable) {
            return false
        }
        
        // TODO: obtain encoding from HTTP header and/or XML document
        String encoding = getDefaultEncoding(grailsApplication);

        if (isXmlType(mediaType)) {
            // Construct domain object from xml map obtained from entity stream
            return type.metaClass.invokeConstructor(xmlToMap(entityStream, encoding))
        } else { // JSON
            // Construct domain object from json map obtained from entity stream
            return type.metaClass.invokeConstructor(JSON.parse(entityStream, encoding))
        }
        
    }

}

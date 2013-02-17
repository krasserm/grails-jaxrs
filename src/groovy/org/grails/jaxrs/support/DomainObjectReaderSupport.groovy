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
package org.grails.jaxrs.support

import static org.grails.jaxrs.support.ConverterUtils.*
import static org.grails.jaxrs.support.ProviderUtils.*

import java.lang.annotation.Annotation
import java.lang.reflect.Type

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyReader

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
 * class NotesResource {*
 *      &#064;POST
 *      &#064;Consumes(['application/xml','application/json'])
 *      Response addNote(Note note) {*          note.save()
 *          // ...
 *}*
 *}*
 *
 * </pre>
 *
 * @author Martin Krasser
 */
abstract class DomainObjectReaderSupport implements MessageBodyReader<Object>, GrailsApplicationAware {

    GrailsApplication grailsApplication

    /**
     * Returns <code>true</code> if <code>type</code> is a Grails domain class
     * and the request content type is one of <code>text/xml</code>,
     * <code>application/xml</code>, <code>text/x-json</code> or
     * <code>application/json</code>. If <code>isEnabled()</code> returns
     * <code>false</code> this method will always return <code>false</code>.
     */
    boolean isReadable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return isEnabled() && grailsApplication.isDomainClass(type) && (isXmlType(mediaType) || isJsonType(mediaType))
    }

    /**
     * Creates a Grails domain object from an XML or JSON request entity
     * stream.
     */
    Object readFrom(Class type, Type genericType,
                    Annotation[] annotations, MediaType mediaType,
                    MultivaluedMap httpHeaders, InputStream entityStream)
    throws IOException, WebApplicationException {

        if (isXmlType(mediaType)) {
            return readFromXml(type, entityStream, getDefaultXMLEncoding(grailsApplication))
        } else { // JSON
            return readFromJson(type, entityStream, getDefaultJSONEncoding(grailsApplication))
        }

    }

    /**
     * If <code>false</code> this reader will be skipped. Returns
     * <code>true</code> by default.
     */
    protected boolean isEnabled() {
        true
    }

    /**
     * Construct domain object from xml map obtained from entity stream.
     */
    protected Object readFromXml(Class type, InputStream entityStream, String charset) {
        def map = xmlToMap(entityStream, charset)
        def result = type.metaClass.invokeConstructor(map)

        // Workaround for http://jira.codehaus.org/browse/GRAILS-1984
        if (!result.id) {
            result.id = idFromMap(map)
        }
        result
    }

    /**
     * Construct domain object from json map obtained from entity stream.
     */
    protected Object readFromJson(Class type, InputStream entityStream, String charset) {
        def map = jsonToDomainConstructionModel(entityStream, charset)
        def result = type.metaClass.invokeConstructor(map)

        // Workaround for http://jira.codehaus.org/browse/GRAILS-1984
        if (!result.id) {
            result.id = idFromMap(map)
        }
        result
    }
}

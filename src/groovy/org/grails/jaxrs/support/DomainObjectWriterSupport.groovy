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
import grails.converters.JSON
import grails.converters.XML

import java.lang.annotation.Annotation
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyWriter

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware

/**
 * A message body writer that converts Grails domain objects to XML or JSON
 * entity streams. The expected response content type is specified by the
 * <code>Accept</code> request header. Supported content types by this
 * provider are <code>text/xml</code>, <code>application/xml</code>,
 * <code>text/x-json</code> or <code>application/json</code>. Assuming
 * <code>Note</code> is a Grails domain class. This provider supports usage of
 * resource methods such as:
 *
 * <pre>
 * &#064;Path('/notes')
 * class NotesResource {
 *
 *      &#064;GET
 *      &#064;Produces(['application/xml','application/json'])
 *      Note getNote(...) {
 *          Note note = ... // e.g. load a note object from database.
 *          return note
 *      }
 *
 *      &#064;GET
 *      &#064;Produces(['application/xml','application/json'])
 *      Collection&lt;Note&gt; getNotes(...) {
 *          def notes = ... // e.g. load list of note objects from database.
 *          return notes
 *      }
 *
 *      &#064;GET
 *      &#064;Produces(['application/xml','application/json'])
 *      def getNotes(...) {
 *          def notes = ... // e.g. load list of note objects from database.
 *          return notes
 *      }
 * }
 *
 * </pre>
 *
 * @author Martin Krasser
 */
abstract class DomainObjectWriterSupport implements MessageBodyWriter<Object>, GrailsApplicationAware {

    GrailsApplication grailsApplication

    long getSize(Object t, Class type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        -1
    }

    /**
     * Returns <code>true</code> if <code>type</code> is a Grails domain class
     * or domain class collection and the <code>mediaType</code> is one of
     * <code>text/xml</code>, <code>application/xml</code>,
     * <code>text/x-json</code> or <code>application/json</code>.
     */
    boolean isWriteable(Class type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {

        if (!isEnabled()) {
            return false
        }

        boolean compatibleMediaType = isXmlType(mediaType) || isJsonType(mediaType)

        if (Collection.isAssignableFrom(type) && !isRequireGenericCollection()) {
            // A potential domain object collection. This check ignores
            // eventual type parameter declarations for reasons of
            // simplicity. It is expected that returned collections
            // from resource methods always contains domain objects.
            return compatibleMediaType
        } else if (genericType instanceof ParameterizedType) {
            return isDomainObjectCollectionType(genericType)
        } else if (grailsApplication.isDomainClass(type)) {
            return compatibleMediaType
        }
        return false
    }

     /**
      * Creates an XML or JSON response entity stream from a Grails domain
      * object.
      */
    void writeTo(Object t, Class type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {

        if (isXmlType(mediaType)) {
            writeToXml(t, entityStream, getDefaultXMLEncoding(grailsApplication))
        } else { // JSON
            writeToJson(t, entityStream, getDefaultJSONEncoding(grailsApplication))
        }
    }

    /**
     * If <code>false</code> this writer will be skipped. Returns
     * <code>true</code> by default.
     */
    protected boolean isEnabled() {
         true
     }

    /**
     * If <code>true</code> resource methods must declare a generic collection
     * type as return type if this provider should handle the return value.
     * The collection type argument must be a domain class. If
     * <code>false</code> this provider handles any returned collection,
     * assuming that the collection elements are domain objects. Default
     * value is <code>false</code> i.e. any collection is handled.
     */
    protected boolean isRequireGenericCollection() {
        false
    }

    /**
     * Creates an XML response entity stream from a Grails domain object.
     */
    protected Object writeToXml(Object t, OutputStream entityStream, String charset) {
        def writer = new OutputStreamWriter(entityStream, charset)
        def converter = new XML(t)
        converter.render(writer)
    }

    /**
     * Creates a JSON response entity stream from a Grails domain object.
     */
    protected Object writeToJson(Object t, OutputStream entityStream, String charset) {
        def writer = new OutputStreamWriter(entityStream, charset)
        def converter = new JSON(t)
        converter.render(writer)
    }

    private boolean isDomainObjectCollectionType(ParameterizedType genericType) {
        if (!Collection.isAssignableFrom(genericType.rawType)) {
            return false
        }
        if (genericType.actualTypeArguments.length == 0) {
            return false
        }
        return grailsApplication.isDomainClass(genericType.actualTypeArguments[0])
    }
}

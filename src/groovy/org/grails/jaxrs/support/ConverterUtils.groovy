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

import grails.converters.JSON
import grails.converters.XML
import groovy.json.JsonSlurper
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grails.databinding.xml.GPathResultMap

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap

import static org.codehaus.groovy.grails.web.converters.configuration.ConvertersConfigurationHolder.getConverterConfiguration

/**
 * Utility class for XML to map conversions.
 *
 * @author Martin Krasser
 */
class ConverterUtils {

    static final LOG = LogFactory.getLog(ConverterUtils)

    /**
     * Returns character encoding settings for the given Grails application.
     *
     * @param application Grails application.
     * @return charset name.
     */
    static String getDefaultEncoding(GrailsApplication application) {
        def encoding = application.config.grails.converters.encoding
        // TODO: use platform encoding if application doesn't provide
        encoding ? encoding : 'UTF-8'
    }

    /**
     * Returns the default XML converter character encoding for the given
     * Grails application.
     */
    static String getDefaultXMLEncoding(GrailsApplication application) {
        def encoding = getConverterConfiguration(XML).encoding
        if (!encoding) {
            return getDefaultEncoding(application)
        }
        encoding
    }

    /**
     * Returns the default JSON converter character encoding for the given
     * Grails application.
     */
    static String getDefaultJSONEncoding(GrailsApplication application) {
        def encoding = getConverterConfiguration(JSON).encoding
        if (!encoding) {
            return getDefaultEncoding(application)
        }
        encoding
    }

    /** Extracts encoding from HTTP headers or MediaType parameters */
    static String getEncoding(MultivaluedMap httpHeaders, MediaType mediaType, String defaultEncoding) {
        // if HTTP headers specify the charset and this was parsed into the MediaType object, return the charset from MediaType
        // Jersey parses http headers into MediaType object
        if (mediaType.parameters['charset']) {
            String encoding = mediaType.parameters['charset'];
            return encoding
        }

        // Restlet does not parse the headers into MediaType so we need to extract them manually
        def contentTypeHeaderKey = httpHeaders.keySet().find { key -> key.toLowerCase() == 'content-type' }
        if (contentTypeHeaderKey) {
            String contentTypeHeaderValue = httpHeaders.getFirst(contentTypeHeaderKey)
            if (contentTypeHeaderValue.contains('charset=')) {
                String encoding = contentTypeHeaderValue.substring(contentTypeHeaderValue.indexOf('charset=') + 'charset='.length())
                return encoding
            }
        }

        // no encoding specified
        return defaultEncoding
    }

    static Map jsonToMap(InputStream input, String encoding) {
        new JsonSlurper().parse(new InputStreamReader(input, encoding))
    }

    /**
     * Reads an XML stream from <code>input</code> and converts it to a map
     * that can be used to construct a Grails domain objects (passing the map
     * to the domain object constructor).
     *
     * @param input XML input stream.
     * @param encoding charset name.
     * @return a map representing the input XML stream.
     */
    static Map xmlToMap(InputStream input, String encoding) {
        new GPathResultMap(new XmlSlurper(false, false).parse(new InputStreamReader(input, encoding)))
    }
}

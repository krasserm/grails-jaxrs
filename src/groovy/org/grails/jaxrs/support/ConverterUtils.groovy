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

import static org.codehaus.groovy.grails.web.converters.configuration.ConvertersConfigurationHolder.getConverterConfiguration

import java.util.Map

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import grails.converters.JSON
import grails.converters.XML

import groovy.util.slurpersupport.GPathResult

import org.apache.commons.logging.*

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.converters.JSONParsingParameterCreationListener;
import org.codehaus.groovy.grails.web.converters.XMLParsingParameterCreationListener;
import org.codehaus.groovy.grails.web.servlet.mvc.ParameterCreationListener
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

/**
 * Utility class for XML to map conversions.
 * 
 * @author Martin Krasser
 */
class ConverterUtils {
    
    static final LOG = LogFactory.getLog(ConverterUtils.class)
    
    private static def jsonListener = new JSONParsingParameterCreationListener() 
    private static def xmlListener = new XMLParsingParameterCreationListener()
    
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
        def encoding = getConverterConfiguration(XML.class).encoding
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
        def encoding = getConverterConfiguration(JSON.class).encoding
        if (!encoding) {
            return getDefaultEncoding(application)
        }
        encoding
    }
	
	/** Extracts encoding from HTTP headers or MediaType parameters */
	static String getEncoding(MultivaluedMap httpHeaders, MediaType mediaType, String defaultEncoding) {
		println httpHeaders
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
    
    /**
     * Reads a JSON stream from <code>input</code> and converts it to a map
     * that can be used to construct a Grails domain objects (passing the map
     * to the domain object constructor).
     * 
     * @param input JSON input stream.
     * @param encoding charset name.
     * @return a map representing the input JSON stream.
     */
    static Map jsonToMap(InputStream input, String encoding) {
        def adapter = new RequestStreamAdapter(input)
        adapter.characterEncoding = encoding
        adapter.format = 'json'
        
        def params = new GrailsParameterMap(adapter)
        jsonListener.paramsCreated(params)
        params.iterator().next().value 
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
        def adapter = new RequestStreamAdapter(input)
        adapter.characterEncoding = encoding
        adapter.format = 'xml'
        
        def params = new GrailsParameterMap(adapter)
        xmlListener.paramsCreated(params)
        params.iterator().next().value 
    }
    
    /**
     * Returns the mapped value for <code>id</code> in <code>map</code> 
     * as java.util.Long or <code>null</code> if there's no mapped value
     * for <code>id</code>.
     * 
     * @throws NumberFormatException if the mapped value for <code>id</code>
     *         cannot be parsed with {@link Long#parseLong}. 
     */
    static Long idFromMap(Map map) {
        if (!map.id) {
            return null
        }
        if (map.id instanceof Number) {
            return map.id.toLong()
        }
        return Long.parseLong(map.id)
    }
    
}

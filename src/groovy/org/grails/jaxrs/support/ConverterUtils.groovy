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

import groovy.json.JsonSlurper

import static org.codehaus.groovy.grails.web.converters.configuration.ConvertersConfigurationHolder.getConverterConfiguration
import grails.converters.JSON
import grails.converters.XML

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest

import org.apache.commons.logging.*
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.mock.web.DelegatingServletInputStream
import org.springframework.mock.web.MockHttpServletRequest

import grails.util.Holders

/**
 * Utility class for XML to map conversions.
 *
 * @author Martin Krasser
 */
class ConverterUtils {

    static final LOG = LogFactory.getLog(ConverterUtils)

    private static ctx = Holders.applicationContext
    private static jsonListener = ctx.containsBean('jsonListener') ? ctx.getBean('jsonListener') : null
    private static xmlListener  = ctx.containsBean('xmlListener')  ? ctx.getBean('xmlListener')  : null

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

    /**
     * Reads a JSON stream from <code>input</code> and converts it to a map
     * that can be used to construct a Grails domain objects (passing the map
     * to the domain object constructor).
     *
     * @param input JSON input stream.
     * @param encoding charset name.
     * @return a map representing the input JSON stream.
     */
    static Map jsonToDomainConstructionModel(InputStream input, String encoding) {
        def adapter = newRequestStreamAdapter(input, encoding, 'json')

        def params = new GrailsParameterMap(adapter)
        jsonListener?.paramsCreated(params)
        params.iterator().next().value
    }

    static Map jsonToSimpleModel(InputStream input, String encoding) {
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
        def adapter = newRequestStreamAdapter(input, encoding, 'xml')

        def params = new GrailsParameterMap(adapter)
        xmlListener?.paramsCreated(params)
        params.iterator().next().value
    }

    /**
     * Returns the mapped value for <code>id</code> in <code>map</code>
     * or <code>null</code> if there's no mapped value for <code>id</code>.
     */
    static idFromMap(Map map) {
        if (!map.id) {
            return null
        } else if (map.id instanceof Number) {
            return map.id.toLong()
        } else if (isNumeric(map.id)) {
            return Long.parseLong(map.id)
        }
        return map.id
    }

    static isNumeric = {
        def formatter = java.text.NumberFormat.instance
        def pos = [0] as java.text.ParsePosition
        formatter.parse(it, pos)

        // if parse position index has moved to end of string
        // them the whole string was numeric
        pos.index == it.size()
    }

    static newRequestStreamAdapter(InputStream stream, String characterEncoding, String format) {

        final MockHttpServletRequest req = new MockHttpServletRequest()

        req.characterEncoding = characterEncoding
        req.setAttribute(GrailsApplicationAttributes.CONTENT_FORMAT, format)

        return (HttpServletRequest) Proxy.newProxyInstance(getClassLoader(),
                [HttpServletRequest, EnhancedRequestProps] as Class[], new InvocationHandler() {
            Object invoke(Object proxy, Method method, Object[] args) {

                String methodName = method.getName()

                if ("getFormat".equals(methodName)) {
                    return req.getAttribute(GrailsApplicationAttributes.CONTENT_FORMAT)
                }

                if ("getInputStream".equals(methodName)) {
                    if (stream instanceof ServletInputStream) {
                        return stream
                    } else if (stream) {
                        return new DelegatingServletInputStream(stream)
                    } else {
                        return req.getInputStream()
                    }
                }
                return req.getClass().getMethod(
                        method.getName(), method.getParameterTypes()).invoke(req, args)
            }
        })
    }

    /**
     * Interface used in order to expose properties dinamically added by grails (i.e. 'format added by RequestMimeTypesApi)
     * Without this some tests would fail.
     */
    interface EnhancedRequestProps {
        String getFormat()

        void setFormat(String)
    }
}

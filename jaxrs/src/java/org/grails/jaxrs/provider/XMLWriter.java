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
package org.grails.jaxrs.provider;

import grails.converters.XML;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.codehaus.groovy.grails.web.converters.configuration.ConvertersConfigurationHolder;
import org.grails.jaxrs.support.MessageBodyWriterSupport;
import org.springframework.stereotype.Component;

/**
 * Provider for Grails' {@link XML} converter class.
 * 
 * @author Martin Krasser
 */
@Component
@Provider
public class XMLWriter extends MessageBodyWriterSupport<XML> {

    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * Renders the <code>xml</code> object to the response's
     * <code>entityStream</code>.
     * 
     * @param xml
     *            XML converter object.
     * @param httpHeaders
     * @param entityStream
     */
    @Override
    protected void writeTo(XML xml, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) 
        throws IOException, WebApplicationException {
        String charset = ConvertersConfigurationHolder.getConverterConfiguration(XML.class).getEncoding();
        Writer writer = null;
        if (charset == null) {
            writer = new OutputStreamWriter(entityStream, DEFAULT_CHARSET);
        } else {
            writer = new OutputStreamWriter(entityStream, charset);
        }
        xml.render(writer);
        writer.flush();

    }
    
}

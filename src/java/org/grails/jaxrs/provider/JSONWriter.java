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

import grails.converters.JSON;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.codehaus.groovy.grails.web.converters.configuration.ConvertersConfigurationHolder;
import org.grails.jaxrs.support.MessageBodyWriterSupport;

/**
 * Provider for Grails' {@link JSON} converter class.
 *
 * Message body writer that supports {@link JSON} response entities. For
 * example, to create a JSON representation from a list of Grails domain
 * objects:
 *
 * <pre>
 * &#064;Path('/notes')
 * &#064;Produces('text/x-json') // or 'application/json'
 * class NotesResource {
 *
 *      &#064;GET
 *      XML findNotes() {
 *          Note.findAll() as JSON
 *      }
 *
 * }
 * </pre>
 *
 * Alternatively, one could write
 *
 * <pre>
 * &#064;Path('/notes')
 * &#064;Produces('text/x-json') // or 'application/json'
 * class NotesResource {
 *
 *      &#064;GET
 *      Response findNotes() {
 *          Response.ok(Note.findAll() as JSON).build()
 *      }
 *
 * }
 * </pre>
 *
 * @author Martin Krasser
 */
@Provider
@Produces({"text/x-json", "application/json"})
public class JSONWriter extends MessageBodyWriterSupport<JSON> {

    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * Renders the <code>json</code> object to the response's
     * <code>entityStream</code> using the encoding set by the Grails
     * application.
     *
     * @param json
     *            JSON object.
     * @param httpHeaders
     *            HTTP headers
     * @param entityStream
     *            JSON response entity stream.
     */
    @Override
    protected void writeTo(JSON json, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
        throws IOException, WebApplicationException {
        String charset = ConvertersConfigurationHolder.getConverterConfiguration(JSON.class).getEncoding();
        Writer writer = null;
        if (charset == null) {
            writer = new OutputStreamWriter(entityStream, DEFAULT_CHARSET);
        } else {
            writer = new OutputStreamWriter(entityStream, charset);
        }
        json.render(writer);
    }
}

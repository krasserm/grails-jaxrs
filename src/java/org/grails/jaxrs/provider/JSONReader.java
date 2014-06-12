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

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware;
import org.grails.jaxrs.support.ConverterUtils;
import org.grails.jaxrs.support.MessageBodyReaderSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import static org.grails.jaxrs.support.ConverterUtils.getDefaultEncoding;
import static org.grails.jaxrs.support.ConverterUtils.jsonToMap;

/**
 * A message body reader that converts a JSON entity stream to a map than can be
 * used to construct Grails domain objects. Any JAX-RS resource method that
 * defines a {@link Map} as parameter and consumes either
 * <code>text/x-json</code> or <code>application/json</code> will be passed a
 * map created from a JSON request entity:
 * <p/>
 * <p/>
 * <pre>
 * &#064;Path('/notes')
 * &#064;Produces('text/x-json')
 * class NotesResource {
 *
 *      &#064;POST
 *      &#064;Consumes('text/x-json')
 *      Response addNote(Map properties) {
 *          // create ne Note domain object
 *          def note = new Note(properties).save()
 *      }
 *
 * }
 *
 *
 * </pre>
 *
 * @author Martin Krasser
 */
@Provider
@Consumes({"text/x-json", "application/json"})
public class JSONReader extends MessageBodyReaderSupport<Map> implements GrailsApplicationAware {

    private GrailsApplication grailsApplication;

    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }

    @Override
    public Map readFrom(Class<Map> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {

        String encoding = ConverterUtils.getEncoding(httpHeaders, mediaType, getDefaultEncoding(grailsApplication));

        // Convert JSON to map used for constructing domain objects
        return jsonToMap(entityStream, encoding);
    }

    @Override
    public Map readFrom(MultivaluedMap<String, String> httpHeaders,
                        InputStream entityStream) throws IOException,
            WebApplicationException {
        // TODO: Fix MessageBodyReaderSupport abstract method (remove it completely or add empty implementation?)
        throw new RuntimeException("This should never be called, because we override the readFrom(all-parameters) method.");
    }
}

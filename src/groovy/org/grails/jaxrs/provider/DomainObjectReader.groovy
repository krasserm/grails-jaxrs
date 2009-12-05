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
 * A concrete domain object reader that provides the same functionality as
 * {@link AbstractDomainObjectReader}. It can be  disabled by setting 
 * <code>org.grails.jaxrs.doreader.disable</code> to <code>true</code> in the 
 * application config.
 * 
 * @see AbstractDomainObjectReader
 *  
 * @author Martin Krasser
 */
@Provider
@Consumes(['text/xml', 'application/xml', 'text/x-json', 'application/json'])
class DomainObjectReader extends AbstractDomainObjectReader {

    // TODO: cleanup imports
     
    protected boolean isEnabled() {
        !ConfigurationHolder.config.org.grails.jaxrs.doreader.disable
    }
    
}

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

import groovy.util.GroovyTestCase

import org.grails.jaxrs.test.integration.CustomRequestEntityReader
import org.grails.jaxrs.test.integration.CustomResponseEntityWriter
import org.grails.jaxrs.test.integration.TestResource02
import org.grails.jaxrs.test.integration.TestResource01
import org.grails.jaxrs.web.JaxrsUtils

/**
 * @author Martin Krasser
 */
abstract class JaxrsControllerIntegrationTests extends GroovyTestCase {

    static List jaxrsClasses = [
               TestResource01.class, 
               TestResource02.class, 
               CustomRequestEntityReader.class, 
               CustomResponseEntityWriter.class
    ]
     
    protected void testGetTest01(def controller) {
        controller.request.method = 'GET'
        controller.request.content = ''.bytes
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/01')
        controller.handle()
        assertEquals(200, controller.response.status)
        assertEquals('test01', controller.response.contentAsString)
        assertTrue(controller.response.getHeader('Content-Type').startsWith('text/plain'))
    }

    protected void testPostTest02(def controller) {
        controller.request.method = 'POST'
        controller.request.content = 'hello'.bytes
        controller.request.addHeader('Content-Type', 'text/plain')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/02')
        controller.handle()
        assertEquals(200, controller.response.status)
        assertEquals('response:hello', controller.response.contentAsString)
        assertTrue(controller.response.getHeader('Content-Type').startsWith('text/plain'))
    }
    
}

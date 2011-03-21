package org.grails.jaxrs
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

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import org.grails.jaxrs.provider.DomainObjectReader
import org.grails.jaxrs.provider.DomainObjectWriter
import org.grails.jaxrs.provider.JSONWriter
import org.grails.jaxrs.provider.JSONReader
import org.grails.jaxrs.test.integration.CustomRequestEntityReader
import org.grails.jaxrs.test.integration.CustomResponseEntityWriter
import org.grails.jaxrs.test.integration.TestResource01
import org.grails.jaxrs.test.integration.TestResource02
import org.grails.jaxrs.test.integration.TestResource03
import org.grails.jaxrs.test.integration.TestResource04
import org.grails.jaxrs.test.integration.TestResource05
import org.grails.jaxrs.web.JaxrsUtils

/**
 * @author Martin Krasser
 */
abstract class JaxrsControllerIntegrationTests extends GroovyTestCase {
    
    static List jaxrsClasses = [
        TestResource01.class, 
        TestResource02.class, 
        TestResource03.class, 
        TestResource04.class, 
        TestResource05.class, 
        CustomRequestEntityReader.class, 
        CustomResponseEntityWriter.class,
        JSONReader.class,        JSONWriter.class,
        DomainObjectReader.class,
        DomainObjectWriter.class
    ]
    
    def controller
    
    void setUp() {
        ConfigurationHolder.config.org.grails.jaxrs.dowriter.require.generic.collections = false
        ConfigurationHolder.config.org.grails.jaxrs.doreader.disable = false
        ConfigurationHolder.config.org.grails.jaxrs.dowriter.disable = false
    }
    
    void testGet01() {
        controller.request.method = 'GET'
        controller.request.content = ''.bytes
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/01')
        controller.handle()
        assertEquals(200, controller.response.status)
        assertEquals('test01', controller.response.contentAsString)
        assertTrue(controller.response.getHeader('Content-Type').startsWith('text/plain'))
    }
    
    void testPost02() {
        controller.request.method = 'POST'
        controller.request.content = 'hello'.bytes
        controller.request.addHeader('Content-Type', 'text/plain')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/02')
        controller.handle()
        assertEquals(200, controller.response.status)
        assertEquals('response:hello', controller.response.contentAsString)
        assertTrue(controller.response.getHeader('Content-Type').startsWith('text/plain'))
    }
    
    void testPost03() {
        controller.request.method = 'POST'
        controller.request.content = '{"class":"TestPerson","age":38,"name":"mike"}'.bytes
        controller.request.addHeader('Content-Type', 'application/json')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/03')
        controller.handle()
        assertEquals(200, controller.response.status)
        assertTrue(controller.response.contentAsString.contains('"age":39'))
        assertTrue(controller.response.contentAsString.contains('"name":"ekim"'))
        assertTrue(controller.response.getHeader('Content-Type').startsWith('application/json'))
    }
    
    void testRoundtrip04XmlSingle() {
        controller.request.method = 'POST'
        controller.request.content = '<testPerson><name>james</name></testPerson>'.bytes
        controller.request.addHeader('Content-Type', 'application/xml')
        controller.request.addHeader('Accept', 'application/xml')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/04/single')
        controller.handle()
        assertEquals(200, controller.response.status)
        assertTrue(controller.response.contentAsString.contains('<name>semaj</name>'))
        assertTrue(controller.response.getHeader('Content-Type').startsWith('application/xml'))
    }
    
    void testRoundtrip04JsonSingle() {
        controller.request.method = 'POST'
        controller.request.content = '{"class":"TestPerson","age":25,"name":"james"}'.bytes
        controller.request.addHeader('Content-Type', 'application/json')
        controller.request.addHeader('Accept', 'application/json')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/04/single')
        controller.handle()
        assertEquals(200, controller.response.status)
        assertTrue(controller.response.contentAsString.contains('"age":26'))
        assertTrue(controller.response.contentAsString.contains('"name":"semaj"'))
        assertTrue(controller.response.getHeader('Content-Type').startsWith('application/json'))
    }
    
    void testGet04XmlCollectionGeneric() {
        controller.request.method = 'GET'
        controller.request.content = ''.bytes
        controller.request.addHeader('Accept', 'application/xml')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/04/multi/generic')
        controller.handle()
        assertEquals(200, controller.response.status)
        assertTrue(controller.response.contentAsString.contains('<list>'))
        assertTrue(controller.response.contentAsString.contains('<name>n1</name>'))
        assertTrue(controller.response.contentAsString.contains('<name>n2</name>'))
        assertTrue(controller.response.getHeader('Content-Type').startsWith('application/xml'))
    }
    
    void testGet04XmlCollectionGenericGenericOnly() {
        ConfigurationHolder.config.org.grails.jaxrs.dowriter.require.generic.collections = true
        testGet04XmlCollectionGeneric()
    }
    
    void testGet04XmlCollectionRaw() {
        controller.request.method = 'GET'
        controller.request.content = ''.bytes
        controller.request.addHeader('Accept', 'application/xml')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/04/multi/raw')
        controller.handle()
        assertEquals(200, controller.response.status)
        assertTrue(controller.response.contentAsString.contains('<list>'))
        assertTrue(controller.response.contentAsString.contains('<name>n1</name>'))
        assertTrue(controller.response.contentAsString.contains('<name>n2</name>'))
        assertTrue(controller.response.getHeader('Content-Type').startsWith('application/xml'))
    }
    
    void testGet04XmlCollectionRawGenericOnly() {
        ConfigurationHolder.config.org.grails.jaxrs.dowriter.require.generic.collections = true
        controller.request.method = 'GET'
        controller.request.content = ''.bytes
        controller.request.addHeader('Accept', 'application/xml')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/04/multi/raw')
        controller.handle()
        assertEquals(500, controller.response.status)
    }
    
    void testGet04XmlCollectionObject() {
        controller.request.method = 'GET'
        controller.request.content = ''.bytes
        controller.request.addHeader('Accept', 'application/xml')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/04/multi/object')
        controller.handle()
        assertEquals(200, controller.response.status)
        assertTrue(controller.response.contentAsString.contains('<list>'))
        assertTrue(controller.response.contentAsString.contains('<name>n1</name>'))
        assertTrue(controller.response.contentAsString.contains('<name>n2</name>'))
        assertTrue(controller.response.getHeader('Content-Type').startsWith('application/xml'))
    }
    
    void testGet04XmlCollectionObjectGenericOnly() {
        ConfigurationHolder.config.org.grails.jaxrs.dowriter.require.generic.collections = true
        controller.request.method = 'GET'
        controller.request.content = ''.bytes
        controller.request.addHeader('Accept', 'application/xml')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/04/multi/object')
        controller.handle()
        assertEquals(500, controller.response.status)
    }
    
    void testGet04JsonCollectionGeneric() {
        controller.request.method = 'GET'
        controller.request.content = ''.bytes
        controller.request.addHeader('Accept', 'application/json')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/04/multi/generic')
        controller.handle()
        assertEquals(200, controller.response.status)
        assertTrue(controller.response.contentAsString.contains('"name":"n1"'))
        assertTrue(controller.response.contentAsString.contains('"name":"n2"'))
        assertTrue(controller.response.getHeader('Content-Type').startsWith('application/json'))
    }
    
    void testPost04ReaderDisabled() {
        ConfigurationHolder.config.org.grails.jaxrs.doreader.disable = true
        controller.request.method = 'POST'
        controller.request.content = '<testPerson><name>james</name></testPerson>'.bytes
        controller.request.addHeader('Content-Type', 'application/xml')
        controller.request.addHeader('Accept', 'application/xml')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/04/single')
        controller.handle()
        assertEquals(415, controller.response.status)
    }
    
    void testPost04WriterDisabled() {
        ConfigurationHolder.config.org.grails.jaxrs.dowriter.disable = true
        controller.request.method = 'POST'
        controller.request.content = '<testPerson><name>james</name></testPerson>'.bytes
        controller.request.addHeader('Content-Type', 'application/xml')
        controller.request.addHeader('Accept', 'application/xml')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/04/single')
        controller.handle()
        assertEquals(500, controller.response.status)
    }
    
    void testPost04DefaultResponse() {
        controller.request.method = 'POST'
        controller.request.content = '<testPerson><name>james</name></testPerson>'.bytes
        controller.request.addHeader('Content-Type', 'application/xml')
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/04/single')
        controller.handle()
        assertEquals(200, controller.response.status)
        println 'default: ' + controller.response.contentAsString
        println 'content: ' + controller.response.getHeader('Content-Type')
    }
    
    void testGet05HtmlResponse() {
        controller.request.method = 'GET'
        controller.request.content = ''.bytes
        JaxrsUtils.setRequestUriAttribute(controller.request, '/test/05')
        controller.handle()
        assertEquals(200, controller.response.status)
        assertEquals('<html><body>test05</body></html>', controller.response.contentAsString)
        assertTrue(controller.response.getHeader('Content-Type').startsWith('text/html'))
    }
}

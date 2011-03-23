package org.grails.jaxrs.itest
/*
 * Copyright 2009 - 2011 the original author or authors.
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

import java.util.List;

import groovy.util.GroovyTestCase

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import org.grails.jaxrs.itest.IntegrationTestCase
import org.junit.Test

import static org.junit.Assert.*

/**
 * @author Martin Krasser
 */
abstract class JaxrsControllerIntegrationTests extends IntegrationTestCase {
    
    // not in grails-app/domain, 
    // so domain classes are added here ...    
    protected List getDomainClasses() {
        [TestPerson.class]
    }

    // not in grails-app/resources or grails-app/providers,
    // so jaxrs classes are added here ...
    protected List getJaxrsClasses() {
        [TestResource01.class,
         TestResource02.class,
         TestResource03.class,
         TestResource04.class,
         TestResource05.class,
         CustomRequestEntityReader.class,
         CustomResponseEntityWriter.class]
    }

    @Test
    void testGet01() {
        sendRequest('/test/01', 'GET')
        assertEquals(200, response.status)
        assertEquals('test01', response.contentAsString)
        assertTrue(response.getHeader('Content-Type').startsWith('text/plain'))
    }
    
    @Test
    void testPost02() {
        sendRequest('/test/02', 'POST', ['Content-Type':'text/plain'], 'hello'.bytes)
        assertEquals(200, response.status)
        assertEquals('response:hello', response.contentAsString)
        assertTrue(response.getHeader('Content-Type').startsWith('text/plain'))
    }
    
    @Test
    void testPost03() {
        sendRequest('/test/03', 'POST', ['Content-Type':'application/json'], '{"class":"TestPerson","age":38,"name":"mike"}'.bytes)
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('"age":39'))
        assertTrue(response.contentAsString.contains('"name":"ekim"'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/json'))
    }
    
    @Test
    void testRoundtrip04XmlSingle() {
        def headers = ['Content-Type':'application/xml', 'Accept':'application/xml']
        sendRequest('/test/04/single', 'POST', headers, '<testPerson><name>james</name></testPerson>'.bytes)
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('<name>semaj</name>'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/xml'))
    }
    
    @Test
    void testRoundtrip04JsonSingle() {
        def headers = ['Content-Type':'application/json', 'Accept':'application/json']
        sendRequest('/test/04/single', 'POST', headers, '{"class":"TestPerson","age":25,"name":"james"}'.bytes)
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('"age":26'))
        assertTrue(response.contentAsString.contains('"name":"semaj"'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/json'))
    }
    
    @Test
    void testGet04XmlCollectionGeneric() {
        sendRequest('/test/04/multi/generic', 'GET', ['Accept':'application/xml'])
        assertTrue(response.contentAsString.contains('<list>'))
        assertTrue(response.contentAsString.contains('<name>n1</name>'))
        assertTrue(response.contentAsString.contains('<name>n2</name>'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/xml'))
    }
    
    @Test
    void testGet04XmlCollectionGenericGenericOnly() {
        ConfigurationHolder.config.org.grails.jaxrs.dowriter.require.generic.collections = true
        testGet04XmlCollectionGeneric()
    }
    
    @Test
    void testGet04XmlCollectionRaw() {
        sendRequest('/test/04/multi/raw', 'GET', ['Accept':'application/xml'])
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('<list>'))
        assertTrue(response.contentAsString.contains('<name>n1</name>'))
        assertTrue(response.contentAsString.contains('<name>n2</name>'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/xml'))
    }
    
    @Test
    void testGet04XmlCollectionRawGenericOnly() {
        ConfigurationHolder.config.org.grails.jaxrs.dowriter.require.generic.collections = true
        sendRequest('/test/04/multi/raw', 'GET', ['Accept':'application/xml'])
        assertEquals(500, response.status)
    }
    
    @Test
    void testGet04XmlCollectionObject() {
        sendRequest('/test/04/multi/object', 'GET', ['Accept':'application/xml'])
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('<list>'))
        assertTrue(response.contentAsString.contains('<name>n1</name>'))
        assertTrue(response.contentAsString.contains('<name>n2</name>'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/xml'))
    }
    
    @Test
    void testGet04XmlCollectionObjectGenericOnly() {
        ConfigurationHolder.config.org.grails.jaxrs.dowriter.require.generic.collections = true
        sendRequest('/test/04/multi/object', 'GET', ['Accept':'application/xml'])
        assertEquals(500, response.status)
    }
    
    @Test
    void testGet04JsonCollectionGeneric() {
        sendRequest('/test/04/multi/generic', 'GET', ['Accept':'application/json'])
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('"name":"n1"'))
        assertTrue(response.contentAsString.contains('"name":"n2"'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/json'))
    }
    
    @Test
    void testPost04ReaderDisabled() {
        ConfigurationHolder.config.org.grails.jaxrs.doreader.disable = true
        sendRequest('/test/04/single', 'POST', ['Content-Type':'application/xml'], '<testPerson><name>james</name></testPerson>'.bytes)
        assertEquals(415, response.status)
    }
    
    @Test
    void testPost04WriterDisabled() {
        ConfigurationHolder.config.org.grails.jaxrs.dowriter.disable = true
        def headers = ['Content-Type':'application/xml', 'Accept':'application/xml']
        sendRequest('/test/04/single', 'POST', headers, '<testPerson><name>james</name></testPerson>'.bytes)
        assertEquals(500, response.status)
    }
    
    @Test
    void testPost04DefaultResponse() {
        sendRequest('/test/04/single', 'POST', ['Content-Type':'application/xml'], '<testPerson><name>james</name></testPerson>'.bytes)
        assertEquals(200, response.status)
        println 'default: ' + response.contentAsString
        println 'content: ' + response.getHeader('Content-Type')
    }
    
    @Test
    void testGet05HtmlResponse() {
        sendRequest('/test/05', 'GET')
        assertEquals(200, response.status)
        assertEquals('<html><body>test05</body></html>', response.contentAsString)
        assertTrue(response.getHeader('Content-Type').startsWith('text/html'))
    }
}

package org.grails.jaxrs.itest

import org.grails.jaxrs.test.*
import org.junit.Test

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

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue
import java.nio.charset.Charset;

import java.util.List;

/**
 * @author Martin Krasser
 */
abstract class JaxrsControllerIntegrationTests extends IntegrationTestCase {

    // not in grails-app/resources or grails-app/providers,
    // so jaxrses are added here ...
    List getJaxrsClasses() {
        [TestResource01,
                TestResource02,
                TestResource03,
                TestResource04,
                TestResource05,
                TestResource06,
                TestQueryParamResource,
                CustomRequestEntityReader,
                CustomResponseEntityWriter]
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
        sendRequest('/test/02', 'POST', ['Content-Type': 'text/plain'], 'hello'.bytes)
        assertEquals(200, response.status)
        assertEquals('response:hello', response.contentAsString)
        assertTrue(response.getHeader('Content-Type').startsWith('text/plain'))
    }

    @Test
    void testPost03() {
        sendRequest('/test/03', 'POST', ['Content-Type': 'application/json'], '{"age":38,"name":"mike"}'.bytes)
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('"age":39'))
        assertTrue(response.contentAsString.contains('"name":"ekim"'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/json'))
    }

    @Test
    void testPost06() {
        sendRequest('/test/06', 'POST', ['Content-Type': 'application/json'], '{"age":38,"name":"mike"}'.bytes)
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('<age>39</age>'))
        assertTrue(response.contentAsString.contains('<name>ekim</name>'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/xml'))
    }

    @Test
    void testPost03_CustomCharset() {
        String CUSTOM_CHARSET = 'UTF-16'
        sendRequest('/test/03', 'POST', ['Content-Type':"application/json; charset=${CUSTOM_CHARSET}".toString()], '{"class":"TestPerson","age":38,"name":"mike"}'.getBytes(CUSTOM_CHARSET))
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('"age":39'))
        assertTrue(response.contentAsString.contains('"name":"ekim"'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/json'))
    }
    
    @Test
    void testRoundtrip04XmlSingle() {
        def headers = ['Content-Type': 'application/xml', 'Accept': 'application/xml']
        sendRequest('/test/04/single', 'POST', headers, '<testPerson><name>james</name></testPerson>'.bytes)
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('<name>semaj</name>'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/xml'))
    }

    @Test
    void testRoundtrip04JsonSingle() {
        def headers = ['Content-Type': 'application/json', 'Accept': 'application/json']
        sendRequest('/test/04/single', 'POST', headers, '{"class":"TestPerson","age":25,"name":"james"}'.bytes)
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('"age":26'))
        assertTrue(response.contentAsString.contains('"name":"semaj"'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/json'))
    }
    
    @Test
    void testRoundtrip04JsonSingle_CustomCharset() {
        String CUSTOM_CHARSET = 'UTF-16'
        def headers = ['Content-Type':"application/json; charset=${CUSTOM_CHARSET}".toString(), 'Accept':'application/json']
        def testPersonWithCustomEncoding = '{"class":"TestPerson","age":45,"name":"james"}'.getBytes(Charset.forName(CUSTOM_CHARSET))
        
        sendRequest('/test/04/single', 'POST', headers, testPersonWithCustomEncoding)
        
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('"age":46'))
        assertTrue(response.contentAsString.contains('"name":"semaj"'))
               assertTrue(response.getHeader('Content-Type').startsWith('application/json'))
    }
    
    @Test
    void testRoundtrip04XmlSingle_CustomCharset() {
        String CUSTOM_CHARSET = 'UTF-16'
        def headers = ['Content-Type':"application/xml; charset=${CUSTOM_CHARSET}".toString(), 'Accept':'application/xml']
        byte[] testPersonWithCustomEncoding = '<testPerson><name>james</name></testPerson>'.getBytes(Charset.forName(CUSTOM_CHARSET))
        
        sendRequest('/test/04/single', 'POST', headers, testPersonWithCustomEncoding)
        
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('<name>semaj</name>'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/xml'))
    }

    @Test
    void testGet04XmlCollectionGeneric() {
        sendRequest('/test/04/multi/generic', 'GET', ['Accept': 'application/xml'])
        assertTrue(response.contentAsString.contains('<list>'))
        assertTrue(response.contentAsString.contains('<name>n1</name>'))
        assertTrue(response.contentAsString.contains('<name>n2</name>'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/xml'))
    }

    @Test
    void testGet04XmlCollectionGenericGenericOnly() {
        grailsApplication.config.org.grails.jaxrs.dowriter.require.generic.collections = true
        testGet04XmlCollectionGeneric()
    }

    @Test
    void testGet04XmlCollectionRaw() {
        sendRequest('/test/04/multi/raw', 'GET', ['Accept': 'application/xml'])
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('<list>'))
        assertTrue(response.contentAsString.contains('<name>n1</name>'))
        assertTrue(response.contentAsString.contains('<name>n2</name>'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/xml'))
    }

    @Test
    void testGet04XmlCollectionRawGenericOnly() {
        grailsApplication.config.org.grails.jaxrs.dowriter.require.generic.collections = true
        sendRequest('/test/04/multi/raw', 'GET', ['Accept': 'application/xml'])
        assertEquals(500, response.status)
    }

    @Test
    void testGet04XmlCollectionObject() {
        sendRequest('/test/04/multi/object', 'GET', ['Accept': 'application/xml'])
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('<list>'))
        assertTrue(response.contentAsString.contains('<name>n1</name>'))
        assertTrue(response.contentAsString.contains('<name>n2</name>'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/xml'))
    }

    @Test
    void testGet04XmlCollectionObjectGenericOnly() {
        grailsApplication.config.org.grails.jaxrs.dowriter.require.generic.collections = true
        sendRequest('/test/04/multi/object', 'GET', ['Accept': 'application/xml'])
        assertEquals(500, response.status)
    }

    @Test
    void testGet04JsonCollectionGeneric() {
        sendRequest('/test/04/multi/generic', 'GET', ['Accept': 'application/json'])
        assertEquals(200, response.status)
        assertTrue(response.contentAsString.contains('"name":"n1"'))
        assertTrue(response.contentAsString.contains('"name":"n2"'))
        assertTrue(response.getHeader('Content-Type').startsWith('application/json'))
    }

    @Test
    void testPost04ReaderDisabled() {
        grailsApplication.config.org.grails.jaxrs.doreader.disable = true
        sendRequest('/test/04/single', 'POST', ['Content-Type': 'application/xml'], '<testPerson><name>james</name></testPerson>'.bytes)
        assertEquals(415, response.status)
    }

    @Test
    void testPost04WriterDisabled() {
        grailsApplication.config.org.grails.jaxrs.dowriter.disable = true
        def headers = ['Content-Type': 'application/xml', 'Accept': 'application/xml']
        sendRequest('/test/04/single', 'POST', headers, '<testPerson><name>james</name></testPerson>'.bytes)
        assertEquals(500, response.status)
    }

    @Test
    void testPost04DefaultResponse() {
        sendRequest('/test/04/single', 'POST', ['Content-Type': 'application/xml'], '<testPerson><name>james</name></testPerson>'.bytes)
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

    @Test
    void specifyQueryParamsOnController() {
        controller.request.queryString = 'value=jim'
        sendRequest('/test/queryParam', 'GET')
        assertEquals(200, response.status)
        assertEquals('jim', response.contentAsString)
    }

    @Test
    void specifyQueryParamsInRequestPath() {
        sendRequest('/test/queryParam?value=jim', 'GET')
        assertEquals(200, response.status)
        assertEquals('jim', response.contentAsString)
    }

    @Test
    void specifyQueryParamsOnControllerAndInRequestPath() {
        controller.request.queryString = 'value=bob'
        sendRequest('/test/queryParam?value=jim', 'GET')
        assertEquals(200, response.status)
        assertEquals('jim', response.contentAsString)
    }
}

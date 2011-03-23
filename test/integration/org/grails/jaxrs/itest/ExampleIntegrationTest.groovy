package org.grails.jaxrs.itest

import javax.servlet.http.HttpServletResponse;

import org.grails.jaxrs.itest.IntegrationTestCase;
import org.junit.Test;

import static org.junit.Assert.*;

class ExampleIntegrationTest extends IntegrationTestCase {

    List getJaxrsClasses() {
        [TestResource01.class,
         TestResource02.class,
         CustomRequestEntityReader.class,
         CustomResponseEntityWriter.class]
    }
    
    @Test
    void testGet() {
        sendRequest('/test/01', 'GET')
        assertEquals(200, response.status)
        assertEquals('test01', response.contentAsString)
        assertTrue(response.getHeader('Content-Type').startsWith('text/plain'))
    }
    
    @Test
    void testPost() {
        sendRequest('/test/02', 'POST', ['Content-Type':'text/plain'], 'hello'.bytes)
        assertEquals(200, response.status)
        assertEquals('response:hello', response.contentAsString)
        assertTrue(response.getHeader('Content-Type').startsWith('text/plain'))
    }

}

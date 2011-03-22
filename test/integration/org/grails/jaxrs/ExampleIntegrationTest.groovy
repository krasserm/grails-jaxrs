package org.grails.jaxrs

import javax.servlet.http.HttpServletResponse;

import org.grails.jaxrs.web.IntegrationTestCase;
import org.grails.jaxrs.web.JaxrsUtils

class ExampleIntegrationTest extends IntegrationTestCase {

    String getContextLocations() {
        'org/grails/jaxrs/context-itest.xml'
    }

    void testGet() {
        sendRequest('/test/01', 'GET')
        assertEquals(200, response.status)
        assertEquals('test01', response.contentAsString)
        assertTrue(response.getHeader('Content-Type').startsWith('text/plain'))
    }
    
    void testPost() {
        sendRequest('/test/02', 'POST', ['Content-Type':'text/plain'], 'hello'.bytes)
        assertEquals(200, response.status)
        assertEquals('response:hello', response.contentAsString)
        assertTrue(response.getHeader('Content-Type').startsWith('text/plain'))
    }

}

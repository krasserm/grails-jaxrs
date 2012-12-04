package org.grails.jaxrs.itest

import static org.junit.Assert.*

import javax.servlet.http.HttpServletResponse

import org.junit.Test

class ExampleIntegrationTest extends IntegrationTestCase {

    List getJaxrsClasses() {
        [TestResource01,
         TestResource02,
         CustomRequestEntityReader,
         CustomResponseEntityWriter]
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

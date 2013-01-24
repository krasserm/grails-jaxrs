package org.grails.jaxrs.itest

import org.grails.jaxrs.test.CustomRequestEntityReader
import org.grails.jaxrs.test.CustomResponseEntityWriter
import org.grails.jaxrs.test.TestResource01
import org.grails.jaxrs.test.TestResource02

/**
 * @author Noam Y. Tenne
 */
class ExampleIntegrationSpec extends IntegrationTestSpec {

    List getJaxrsClasses() {
        [TestResource01,
                TestResource02,
                CustomRequestEntityReader,
                CustomResponseEntityWriter]
    }

    def "Execute a GET request"() {
        when:
        sendRequest('/test/01', 'GET')

        then:
        response.status == 200
        response.contentAsString == 'test01'
        response.getHeader('Content-Type').startsWith('text/plain')
    }

    def "Execute a POST request"() {
        when:
        sendRequest('/test/02', 'POST', ['Content-Type': 'text/plain'], 'hello'.bytes)

        then:
        response.status == 200
        response.contentAsString == 'response:hello'
        response.getHeader('Content-Type').startsWith('text/plain')
    }
}

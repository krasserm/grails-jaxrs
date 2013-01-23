package org.grails.jaxrs.itest

import org.grails.jaxrs.test.CustomRequestEntityReader
import org.grails.jaxrs.test.CustomResponseEntityWriter
import org.grails.jaxrs.test.TestResource01
import org.grails.jaxrs.test.TestResource02
import org.grails.jaxrs.test.TestResource03
import org.grails.jaxrs.test.TestResource04
import org.grails.jaxrs.test.TestResource05
import org.grails.jaxrs.test.TestResource06

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
/**
 * @author Noam Y. Tenne
 */
abstract class JaxrsControllerIntegrationSpec extends IntegrationTestSpec {

    // not in grails-app/resources or grails-app/providers,
    // so jaxrses are added here ...
    List getJaxrsClasses() {
        [TestResource01,
                TestResource02,
                TestResource03,
                TestResource04,
                TestResource05,
                TestResource06,
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

    def "Execute a POST request on resource 02"() {
        when:
        sendRequest('/test/02', 'POST', ['Content-Type': 'text/plain'], 'hello'.bytes)

        then:
        response.status == 200
        response.contentAsString == 'response:hello'
        response.getHeader('Content-Type').startsWith('text/plain')
    }

    def "Execute a POST request on resource 03"() {
        when:
        sendRequest('/test/03', 'POST', ['Content-Type': 'application/json'], '{"class":"TestPerson","age":38,"name":"mike"}'.bytes)

        then:
        response.status == 200
        response.contentAsString.contains('"age":39')
        response.contentAsString.contains('"name":"ekim"')
        response.getHeader('Content-Type').startsWith('application/json')
    }

    def "Execute a POST request on resource 06"() {
        when:
        sendRequest('/test/06', 'POST', ['Content-Type': 'application/json'], '{"class":"TestPerson","age":38,"name":"mike"}'.bytes)

        then:
        response.status == 200
        response.contentAsString.contains('<age>39</age>')
        response.contentAsString.contains('<name>ekim</name>')
        response.getHeader('Content-Type').startsWith('application/xml')
    }

    def "Initiate a single round-trip on resource 04"() {
        setup:
        def headers = ['Content-Type': contentType, 'Accept': contentType]

        when:
        sendRequest('/test/04/single', 'POST', headers, postedContent.bytes)

        then:
        response.status == 200
        expetedPartialReturnedContent.each {
            assert response.contentAsString.contains(it)
        }
        response.getHeader('Content-Type').startsWith(contentType)

        where:
        contentType        | postedContent                                    | expetedPartialReturnedContent
        'application/xml'  | '<testPerson><name>james</name></testPerson>'    | ['<name>semaj</name>']
        'application/json' | '{"class":"TestPerson","age":25,"name":"james"}' | ['"name":"semaj"', '"age":26']
    }

    def "Retrieve a generic XML collection from resource 04"() {
        setup:
        grailsApplication.config.org.grails.jaxrs.dowriter.require.generic.collections = genericOnly

        when:
        sendRequest('/test/04/multi/generic', 'GET', ['Accept': 'application/xml'])

        then:
        response.contentAsString.contains('<list>')
        response.contentAsString.contains('<name>n1</name>')
        response.contentAsString.contains('<name>n2</name>')
        response.getHeader('Content-Type').startsWith('application/xml')

        where:
        genericOnly << [true, false]
    }

    def "Retrieve a raw and object XML collection from resource 04"() {
        when:
        sendRequest("/test/04/multi/$mode", 'GET', ['Accept': 'application/xml'])

        then:
        response.status == 200
        response.contentAsString.contains('<list>')
        response.contentAsString.contains('<name>n1</name>')
        response.contentAsString.contains('<name>n2</name>')
        response.getHeader('Content-Type').startsWith('application/xml')

        where:
        mode << ['raw', 'object']
    }

    def "Retrieve a raw and object XML collection from resource 04 with generic-only turned on"() {
        setup:
        grailsApplication.config.org.grails.jaxrs.dowriter.require.generic.collections = true

        when:
        sendRequest("/test/04/multi/$mode", 'GET', ['Accept': 'application/xml'])

        then:
        response.status == 500

        where:
        mode << ['raw', 'object']
    }

    def "Retrieve a generic JSON collection from resource 04"() {
        when:
        sendRequest('/test/04/multi/generic', 'GET', ['Accept': 'application/json'])

        then:
        response.contentAsString.contains('"name":"n1"')
        response.contentAsString.contains('"name":"n2"')
        response.getHeader('Content-Type').startsWith('application/json')
    }

    def "Post content to resource 04 while the IO facilities are disabled"() {
        setup:
        def headers = ['Content-Type': 'application/xml', 'Accept': 'application/xml']
       grailsApplication.config.org.grails.jaxrs.getProperty("do${facilityToDisabled}").disable = true

        when:
        sendRequest('/test/04/single', 'POST', headers, '<testPerson><name>james</name></testPerson>'.bytes)

        then:
        response.status == expectedStatus

        where:
        facilityToDisabled | expectedStatus
        'reader'           | 415
        'writer'           | 500
    }

    def "Print the default response of a POST request to resource 04"() {
        when:
        sendRequest('/test/04/single', 'POST', ['Content-Type': 'application/xml'], '<testPerson><name>james</name></testPerson>'.bytes)

        then:
        response.status == 200
        println 'default: ' + response.contentAsString
        println 'content: ' + response.getHeader('Content-Type')
    }

    def "Retrieve an HTML response from resource 05"() {
        when:
        sendRequest('/test/05', 'GET')

        then:
        response.status == 200
        response.contentAsString == '<html><body>test05</body></html>'
        response.getHeader('Content-Type').startsWith('text/html')
    }
}

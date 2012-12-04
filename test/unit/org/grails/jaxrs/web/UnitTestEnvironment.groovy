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
package org.grails.jaxrs.web

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.mock.web.MockHttpServletResponse

/**
 * @author Martin Krasser
 */
class UnitTestEnvironment {

    private JaxrsContext jaxrsContext

    synchronized JaxrsContext getJaxrsContext() {
        if (!jaxrsContext) {
            jaxrsContext = new JaxrsContext()
            jaxrsContext.init(new MockJaxrsServlet())
        }
        jaxrsContext
    }
}

/**
 * @author Martin Krasser
 */
class MockJaxrsServlet extends HttpServlet {

    void service(HttpServletRequest request, HttpServletResponse response) {
        MockHttpServletResponse mockResponse = (MockHttpServletResponse)response
        mockResponse.writer.println(request.requestURI)
        mockResponse.writer.flush()
    }

    void init(ServletConfig config) {
        // nothing to do
    }
}

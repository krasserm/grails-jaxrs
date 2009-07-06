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
package org.grails.jaxrs.test.unit;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Martin Krasser
 */
public class TestEnvironment {

    private static TestEnvironment instance;
    
    private Servlet mockServlet;
    
    public synchronized static TestEnvironment getInstance() {
        if (instance == null) {
            instance = new TestEnvironment();
            instance.mockServlet = new MockServlet();
        }
        return instance;
    }
    
    public Servlet getMockServlet() {
        return mockServlet;
    }
    
    @SuppressWarnings("serial")
    private static class MockServlet extends HttpServlet {

        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
            MockHttpServletResponse mockResponse = (MockHttpServletResponse)response;
            mockResponse.getWriter().println(request.getRequestURI());
            mockResponse.getWriter().flush();
        }

    }
    
}

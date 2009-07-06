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
package org.grails.jaxrs.test.integration;

import javax.servlet.Servlet;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.grails.jaxrs.web.JaxrsListener;
import org.grails.jaxrs.web.JaxrsUtils;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ContextLoaderListener;

/**
 * @author Martin Krasser
 */
public class TestEnvironment {

    private static TestEnvironment instance;
    
    private Servlet jaxrsServlet;
    
    public synchronized static TestEnvironment getInstance() {
        if (instance == null) {
            MockServletContext mockServletContext = new MockServletContext();
            mockServletContext.addInitParameter("contextConfigLocation", "context-integration.xml");
            
            ServletContextListener loaderListener = new ContextLoaderListener();
            ServletContextListener jaxrsListener = new JaxrsListener();
            
            loaderListener.contextInitialized(new ServletContextEvent(mockServletContext));
            jaxrsListener.contextInitialized(new ServletContextEvent(mockServletContext));
            
            instance = new TestEnvironment();
            instance.jaxrsServlet = JaxrsUtils.getJaxrsServlet(mockServletContext);

        }
        return instance;
    }
    
    public Servlet getJaxrsServlet() {
        return jaxrsServlet;
    }
    
}

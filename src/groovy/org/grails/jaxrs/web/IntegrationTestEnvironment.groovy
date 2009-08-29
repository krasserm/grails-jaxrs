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

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ContextLoaderListener;

/**
 * @author Martin Krasser
 */
class IntegrationTestEnvironment {

    private JaxrsContext jaxrsContext
    
    private String contextConfigLocation
    
    private String jaxrsProviderName
    
    private List jaxrsClasses
    
    IntegrationTestEnvironment(String contextConfigLocation, String jaxrsProviderName, List jaxrsClasses) {
        this.contextConfigLocation = contextConfigLocation
        this.jaxrsProviderName = jaxrsProviderName
        this.jaxrsClasses = jaxrsClasses
    }
    
    synchronized JaxrsContext getJaxrsContext() {
        if (!jaxrsContext) {
            MockServletContext mockServletContext = new MockServletContext()
            mockServletContext.addInitParameter('contextConfigLocation', contextConfigLocation)
            
            ServletContextListener loaderListener = new ContextLoaderListener()
            ServletContextListener jaxrsListener = new JaxrsListener()
            
            loaderListener.contextInitialized(new ServletContextEvent(mockServletContext))
            jaxrsListener.contextInitialized(new ServletContextEvent(mockServletContext))

            jaxrsContext= JaxrsUtils.getRequiredJaxrsContext(mockServletContext)
            jaxrsContext.jaxrsServletContext = mockServletContext
            
            jaxrsClasses.each { jaxrsContext.jaxrsConfig.classes << it }
            jaxrsContext.jaxrsProviderName = jaxrsProviderName
            jaxrsContext.init()
        }
        jaxrsContext
    }
    
}

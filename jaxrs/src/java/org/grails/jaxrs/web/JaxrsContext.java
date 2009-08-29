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
package org.grails.jaxrs.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * The JAX-RS context used by applications to interact with a JAX-RS
 * implementation.
 * 
 * @author Martin Krasser
 */
public class JaxrsContext {

    public static final String JAXRS_PROVIDER_NAME_JERSEY = "jersey";
    public static final String JAXRS_PROVIDER_NAME_RESTLET = "restlet";
    
    /**
     * Name of the JAX-RS servlet.
     */
    public static final String SERVLET_NAME = "org.grails.jaxrs.servlet.name";

    private volatile Servlet jaxrsServlet;
    private volatile ServletContext jaxrsServletContext;
    private volatile JaxrsConfig jaxrsConfig;
    private volatile String jaxrsProviderName;
    
    private JaxrsService jaxrsService;
    
    public JaxrsContext() {
        this.jaxrsConfig = new JaxrsConfig();
        this.jaxrsService = new JaxrsServiceImpl();
        this.jaxrsProviderName = JAXRS_PROVIDER_NAME_JERSEY;
    }

    public JaxrsConfig getJaxrsConfig() {
        return jaxrsConfig;
    }
    
    /**
     * Returns a service instance for processing HTTP requests.
     * 
     * @return a {@link JaxrsService} instance.
     */
    public JaxrsService getJaxrsService() {
        return jaxrsService;
    }

    public void setJaxrsProviderName(String jaxrsProviderName) {
        this.jaxrsProviderName = jaxrsProviderName;
    }
    
    /**
     * Reloads the JAX-RS configuration made by Grails applications and
     * re-initializes the JAX-RS runtime.
     * 
     * @throws ServletException
     * @throws IOException
     */
    public void refresh() throws ServletException, IOException {
        if (jaxrsServlet != null) {
            destroy();
            init();
        }
    }
    
    void init() throws ServletException {
        if (jaxrsProviderName.equals(JAXRS_PROVIDER_NAME_RESTLET)) {
            init(new RestletServlet(jaxrsConfig));
        } else if (jaxrsProviderName.equals(JAXRS_PROVIDER_NAME_JERSEY)) {
            init(new SpringServlet());
        } else {
            throw new JaxrsException("Illegal provider name: " + jaxrsProviderName + ". either use "
                    + JAXRS_PROVIDER_NAME_JERSEY + " or " 
                    + JAXRS_PROVIDER_NAME_RESTLET + "."); 
        }
                
    }
    
    void init(Servlet jaxrsServlet) throws ServletException {
        this.jaxrsServlet = jaxrsServlet;
        this.jaxrsServlet.init(new Config());
    }
    
    void destroy() {
        jaxrsServlet.destroy();
        jaxrsServlet = null;
    }
    
    void setJaxrsServletContext(ServletContext jaxrsServletContext) {
        this.jaxrsServletContext = jaxrsServletContext;
    }
    
    private class JaxrsServiceImpl implements JaxrsService {

        @Override
        public void process(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
            jaxrsServlet.service(request, response);
        }
        
    }
    
    private class Config implements ServletConfig {
        
        public String getInitParameter(String name) {
            return null;
        }

        public Enumeration<String> getInitParameterNames() {
            return new EmptyEnumeration();
        }

        public ServletContext getServletContext() {
            return jaxrsServletContext;
        }

        public String getServletName() {
            return SERVLET_NAME;
        }
        
    }
    
    private static class EmptyEnumeration implements Enumeration<String> {

        public boolean hasMoreElements() {
            return false;
        }

        public String nextElement() {
            throw new NoSuchElementException();
        }
        
    }
    
}

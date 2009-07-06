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

import static org.grails.jaxrs.web.JaxrsUtils.*;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * Initializes a Jersey servlet for request processing.  
 * 
 * @author Martin Krasser
 */
public class JaxrsListener implements ServletContextListener {

    private static final Log LOG = LogFactory.getLog(JaxrsListener.class);
    
    public void contextDestroyed(ServletContextEvent event) {
        // TODO: implement jersey servlet destruction
    }

    /**
     * Initializes a Jersey servlet for request processing and stores the
     * initialized servlet in the servlet context for later reference by the
     * JAX-RS controller. The servlet is only accessible via the JAX-RS
     * controller.
     * 
     * @see JaxrsUtils#SERVLET_NAME.
     */
    public void contextInitialized(ServletContextEvent event) {
        try {
            ServletContext context = event.getServletContext();
            Servlet jerseyServlet = new SpringServlet();
            jerseyServlet.init(new Config(context));
            setJaxrsServlet(context, jerseyServlet);
            LOG.info("JAX-RS servlet initialized");
        } catch (ServletException e) {
            LOG.error("Initialization of JAX-RS servlet failed", e);
        }        
    }

    private static class Config implements ServletConfig {

        private ServletContext context;
        
        public Config(ServletContext context) {
            this.context = context;
        }
        
        public String getInitParameter(String name) {
            return null;
        }

        public Enumeration<String> getInitParameterNames() {
            return new EmptyEnumeration();
        }

        public ServletContext getServletContext() {
            return context;
        }

        public String getServletName() {
            return SERVLET_NAME;
        }
        
    }
    
    private static class EmptyEnumeration implements Enumeration<String> {

        @Override
        public boolean hasMoreElements() {
            return false;
        }

        @Override
        public String nextElement() {
            throw new NoSuchElementException();
        }
        
    }
    
}

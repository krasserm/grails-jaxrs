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

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

/**
 * @author Martin Krasser
 */
public class JaxrsUtils {

    /**
     * Name of the JAX-RS servlet as well as the name of the attribute for
     * storing the servlet in the servlet context.
     */
    public static final String SERVLET_NAME = 
        "org.grails.jaxrs.servlet.name";

    /**
     * Name of the request attribute for storing the request URI. 
     */
    public static final String REQUEST_URI_ATTRIBUTE_NAME = 
        "org.grails.jaxrs.request.uri";
    
    
    public static Servlet getJaxrsServlet(ServletContext context) {
        return (Servlet)context.getAttribute(SERVLET_NAME);
    }
    
    public static void setJaxrsServlet(ServletContext context, Servlet servlet) {
        context.setAttribute(SERVLET_NAME, servlet);
    }
    
    public static String getRequestUriAttribute(ServletRequest request) {
        return (String)request.getAttribute(REQUEST_URI_ATTRIBUTE_NAME);
    }
    
    public static void setRequestUriAttribute(ServletRequest request, String uri) {
        request.setAttribute(REQUEST_URI_ATTRIBUTE_NAME, uri);
    }
    
}

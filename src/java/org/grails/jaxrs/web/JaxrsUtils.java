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

import static org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.springframework.context.ApplicationContext;

/**
 * @author Martin Krasser
 */
public class JaxrsUtils {

    /**
     * Bean name of the JAX-RS context.
     */
    public static final String JAXRS_CONTEXT_NAME = "jaxrsContext";

    /**
     * Name of the request attribute for storing the request URI.
     */
    public static final String REQUEST_URI_ATTRIBUTE_NAME =
        "org.grails.jaxrs.request.uri";

    /**
     * Obtains the request URI that has been previously been stored via
     * {@link #setRequestUriAttribute(ServletRequest, String)} from a
     * <code>request</code>.
     *
     * @param request
     *            request where to obtain the URI from.
     * @return request URI.
     */
    public static String getRequestUriAttribute(ServletRequest request) {
        return (String)request.getAttribute(REQUEST_URI_ATTRIBUTE_NAME);
    }

    /**
     * Stores a request <code>uri</code> as <code>request</code> attribute. The
     * request attribute name is {@link #REQUEST_URI_ATTRIBUTE_NAME}.
     *
     * @param request
     *            request where to store the URI.
     * @param uri
     *            request URI.
     */
    public static void setRequestUriAttribute(ServletRequest request, String uri) {
        request.setAttribute(REQUEST_URI_ATTRIBUTE_NAME, uri);
    }

    /**
     * Returns the {@link JaxrsContext} for the given servlet context.
     *
     * @param servletContext
     *            servlet context.
     * @return a {@link JaxrsContext} instance.
     */
    public static JaxrsContext getRequiredJaxrsContext(ServletContext servletContext) {
        ApplicationContext ac = getRequiredWebApplicationContext(servletContext);
        return (JaxrsContext)ac.getBean(JAXRS_CONTEXT_NAME, JaxrsContext.class);
    }
}

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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A servlet filter that stores the request URI as request attribute.
 *
 * @author Martin Krasser
 *
 * @see RequestWrapper
 */
public class JaxrsFilter extends OncePerRequestFilter {

    /**
     * Stores the request URI as request attribute.
     *
     * @see JaxrsUtils#REQUEST_URI_ATTRIBUTE_NAME
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        JaxrsUtils.setRequestUriAttribute(request, request.getRequestURI());
        chain.doFilter(request, response);
    }
}

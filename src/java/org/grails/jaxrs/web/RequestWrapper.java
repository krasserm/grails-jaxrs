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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * A request wrapper that decorates {@link HttpServletRequest#getRequestURI()}.
 *
 * @author Martin Krasser
 *
 * @see JaxrsFilter
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    public RequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * Returns the request URI based on the attribute written by
     * {@link JaxrsFilter}.
     *
     * @return the request URI.
     *
     * @see JaxrsUtils#REQUEST_URI_ATTRIBUTE_NAME
     */
    @Override
    public String getRequestURI() {
        return JaxrsUtils.getRequestUriAttribute(getRequest());
    }

    /**
     * Always returns the empty string.
     *
     * @return an empty string.
     */
    @Override
    public String getServletPath() {
        return "";
    }
}

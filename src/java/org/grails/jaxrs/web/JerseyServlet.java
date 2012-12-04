/*
 * Copyright 2009-2010 the original author or authors.
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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.ws.rs.ext.Provider;

import org.grails.jaxrs.web.JaxrsContext.Config;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * Servlet that dispatches JAX-RS requests to Jersey.
 *
 * @author Martin Krasser
 * @author David Castro
 */
@SuppressWarnings("serial")
public class JerseyServlet extends SpringServlet {

    private static final String PROVIDER_EXTRA_PATHS_KEY = "com.sun.jersey.config.property.packages";

    /**
     * Initializes the Jersey servlet. If <code>config</code> is an instance of
     * {@link Config} then Jersey is configured with extra paths for scanning
     * {@link Provider} classes.
     *
     * @param servletConfig
     *
     * @see Config#getJaxrsProviderExtraPaths()
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        if (servletConfig instanceof Config) {
            init((Config)servletConfig);
        }
        super.init(servletConfig);
    }

    /**
     * Sets the <code>com.sun.jersey.config.property.packages</code> init
     * parameter to the extra path defined by <code>config</code>. The extra
     * path is a semicolon-delimited list of packages which Jersey should scan
     * for additional {@link Provider} classes.
     *
     * @see Config#getJaxrsProviderExtraPaths()
     */
    void init(Config config) {
        String extra = config.getJaxrsProviderExtraPaths();
        if (isExtraPathsDefined(extra) && !config.getInitParameters().containsKey(PROVIDER_EXTRA_PATHS_KEY)) {
            config.getInitParameters().put(PROVIDER_EXTRA_PATHS_KEY, extra);
        }
    }

    private static boolean isExtraPathsDefined(String extra) {
        return extra != null && !extra.isEmpty();
    }
}

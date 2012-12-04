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

import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.ext.jaxrs.InstantiateException;
import org.restlet.ext.jaxrs.JaxRsApplication;
import org.restlet.ext.jaxrs.ObjectFactory;
import org.restlet.ext.servlet.ServerServlet;
import org.springframework.context.ApplicationContext;

/**
 * Servlet that dispatches JAX-RS requests to Restlet.
 *
 * @author Martin Krasser
 */
@SuppressWarnings("serial")
public class RestletServlet extends ServerServlet {

    private JaxrsConfig config;

    /**
     * Creates a new {@link RestletServlet}
     *
     * @param config
     *            JAX-RS configuration of the current {@link JaxrsContext}.
     */
    public RestletServlet(JaxrsConfig config) {
        this.config = config;
    }

    /**
     * Returns the JAX-RS configuration.
     *
     * @return the JAX-RS configuration.
     */
    public JaxrsConfig getConfig() {
        return config;
    }

    //
    // TODO: set servlet config init parameters ...
    //

    /**
     * Destroys this servlet removing all Restlet-specific attributes from the
     * servlet context.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void destroy() {
        Enumeration<String> names = getServletContext().getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (name.startsWith("org.restlet")) {
                getServletContext().removeAttribute(name);
            }
        }
        super.destroy();

    }

    /**
     * Creates a {@link JaxRsApplication} for the given Restlet parent context.
     * A custom object factory is provided to lookup JAX-RS resource and
     * provider objects from the Spring web application context.
     *
     * @param Restlet
     *            parent context.
     * @return a new {@link JaxRsApplication} instance.
     */
    @Override
    protected Application createApplication(Context parentContext) {
        JaxRsApplication jaxRsApplication = new JaxRsApplication(parentContext.createChildContext());
        jaxRsApplication.setObjectFactory(new ApplicationContextObjectFactory(getServletContext()));
        jaxRsApplication.add(config);
        return jaxRsApplication;
    }

    private static class ApplicationContextObjectFactory implements ObjectFactory {

        ApplicationContext applicationContext;

        public ApplicationContextObjectFactory(ServletContext servletContext) {
            this.applicationContext = getRequiredWebApplicationContext(servletContext);
        }

        public <T> T getInstance(Class<T> jaxRsClass) throws InstantiateException {
            // TODO: make this implementation more robust (plus improved performance)
            return (T)applicationContext.getBeansOfType(jaxRsClass).values().iterator().next();
        }
    }
}

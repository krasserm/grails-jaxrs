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
 * @author Martin Krasser
 */
@SuppressWarnings("serial")
public class RestletServlet extends ServerServlet {

    private JaxrsConfig config;

    public RestletServlet(JaxrsConfig config) {
        this.config = config;
    }
    
    public JaxrsConfig getConfig() {
        return config;
    }

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

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getInstance(Class<T> jaxRsClass) throws InstantiateException {
            // TODO: make this implementation more robust (plus improved performance)
            return (T)applicationContext.getBeansOfType(jaxRsClass).values().iterator().next();
        }
        
    }
    
}

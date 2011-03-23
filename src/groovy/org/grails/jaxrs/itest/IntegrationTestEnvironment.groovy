/*
 * Copyright 2009 - 2011 the original author or authors.
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
package org.grails.jaxrs.itest

import grails.spring.BeanBuilder;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler;
import org.grails.jaxrs.ProviderArtefactHandler;
import org.grails.jaxrs.ResourceArtefactHandler;
import org.grails.jaxrs.web.JaxrsContext;
import org.grails.jaxrs.web.JaxrsListener;
import org.grails.jaxrs.web.JaxrsUtils;

import org.springframework.mock.web.MockServletContext;

/**
 * @author Martin Krasser
 */
class IntegrationTestEnvironment {

    private JaxrsContext jaxrsContext
    
    private String contextConfigLocations
    private String jaxrsProviderName

    private List jaxrsClasses
    private List domainClasses
    
    private boolean autoDetectJaxrsClasses
    private boolean autoDetectDomainClasses
    
    IntegrationTestEnvironment(String contextConfigLocations, String jaxrsProviderName, 
            List jaxrsClasses, 
            List domainClasses,
            boolean autoDetectJaxrsClasses,
            boolean autoDetectDomainClasses) {

        this.contextConfigLocations = contextConfigLocations
        this.jaxrsProviderName = jaxrsProviderName
        this.jaxrsClasses = jaxrsClasses
        this.domainClasses = domainClasses
        this.autoDetectJaxrsClasses = autoDetectJaxrsClasses
        this.autoDetectDomainClasses = autoDetectDomainClasses
    }
    
    synchronized JaxrsContext getJaxrsContext() {
        if (!jaxrsContext) {
            MockServletContext mockServletContext = new MockServletContext()
            mockServletContext.addInitParameter('contextConfigLocation', "org/grails/jaxrs/itest/IntegrationTestEnvironment.xml, ${contextConfigLocations}")
            
            IntegrationTestContextLoaderListener loaderListener = new IntegrationTestContextLoaderListener()
            ServletContextListener jaxrsListener = new JaxrsListener()
            
            loaderListener.contextInitialized(new ServletContextEvent(mockServletContext))

            if (autoDetectJaxrsClasses) {
                IntegrationTestApplication.instance.pluginApplication.getArtefacts(ResourceArtefactHandler.TYPE).each { gc ->
                     jaxrsClasses << gc.clazz
                }
                IntegrationTestApplication.instance.pluginApplication.getArtefacts(ProviderArtefactHandler.TYPE).each { gc ->
                     jaxrsClasses << gc.clazz
                }
            }
            
            if (autoDetectDomainClasses) {
                IntegrationTestApplication.instance.pluginApplication.getArtefacts(DomainClassArtefactHandler.TYPE).each { gc ->
                     domainClasses << gc.clazz
                }
            }

            new BeanBuilder().beans {
                jaxrsClasses.each { clazz -> 
                   "${clazz.name}"(clazz)
                }
            }.registerBeans(loaderListener.webApplicationContext.beanFactory)
            
            jaxrsListener.contextInitialized(new ServletContextEvent(mockServletContext))            
            jaxrsContext = JaxrsUtils.getRequiredJaxrsContext(mockServletContext)
            jaxrsContext.jaxrsServletContext = mockServletContext
            
            domainClasses.each { IntegrationTestApplication.instance.domainClasses << it }
            jaxrsClasses.each { jaxrsContext.jaxrsConfig.classes << it }
            
            jaxrsContext.jaxrsProviderName = jaxrsProviderName
            jaxrsContext.init()            
        }
        jaxrsContext
    }
    
}

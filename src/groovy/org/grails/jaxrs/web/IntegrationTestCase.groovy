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
 package org.grails.jaxrs.web

import javax.servlet.http.HttpServletResponse;

import groovy.util.GroovyTestCase;

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import org.grails.jaxrs.JaxrsController;

/**
 * Base class for integration testing JAX-RS resources and providers.  
 * 
 * @author Martin Krasser
 */
abstract class IntegrationTestCase extends GroovyTestCase {

    static transactional = false
    static environment

    def controller
    
    void setRequestUrl(String url) {
        JaxrsUtils.setRequestUriAttribute(controller.request, url)
    }
    
    void setRequestMethod(String method) {
        controller.request.method = method
    }
    
    void setRequestContent(byte[] content) {
        controller.request.content = content
    }

    void addRequestHeader(String key, Object value) {
        controller.request.addHeader(key, value)
    }

    void resetResponse() {
        controller.response.committed = false
        controller.response.reset()
    }
    
    HttpServletResponse getResponse() {
        controller.response
    }
    
    HttpServletResponse sendRequest(String url, String method, byte[] content = ''.bytes) {
        sendRequest(url, method, [:], content)
    }
        
    HttpServletResponse sendRequest(String url, String method, Map<String, Object> headers, byte[] content = ''.bytes) {
        resetResponse()
        
        requestUrl = url
        requestMethod = method
        requestContent = content

        headers.each { entry ->  
            addRequestHeader(entry.key, entry.value)
        }
        
        controller.handle()
        controller.response
    }
        
    void setUp() {
        ConfigurationHolder.config.org.grails.jaxrs.dowriter.require.generic.collections = false
        ConfigurationHolder.config.org.grails.jaxrs.doreader.disable = false
        ConfigurationHolder.config.org.grails.jaxrs.dowriter.disable = false

        if (!environment) {
            environment = new IntegrationTestEnvironment(contextLocations, jaxrsImplementation, jaxrsClasses)
        }

        controller = new JaxrsController()
        controller.jaxrsContext = environment.jaxrsContext
    }
    
    protected String getContextLocations() {
        ''
    }
    
    protected String getJaxrsImplementation() {
        'jersey'
    }
    
    protected List getJaxrsClasses() {
        []
    }
    
}

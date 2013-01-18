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

import javax.servlet.http.HttpServletResponse

import org.grails.jaxrs.JaxrsController
import org.grails.jaxrs.web.JaxrsContext
import org.grails.jaxrs.web.JaxrsUtils
import org.junit.Before
import org.junit.BeforeClass

/**
 * Base class for integration testing JAX-RS resources and providers.
 *
 * @author Martin Krasser
 */
abstract class IntegrationTestCase {

    def grailsApplication

    static transactional = false
    static testEnvironment

    def controller

    @BeforeClass
    static void setUpBeforeClass() {
        testEnvironment = null
    }

    @Before
    void setUp() {
        grailsApplication.config.org.grails.jaxrs.dowriter.require.generic.collections = false
        grailsApplication.config.org.grails.jaxrs.doreader.disable = false
        grailsApplication.config.org.grails.jaxrs.dowriter.disable = false

        if (!testEnvironment) {
            testEnvironment = new IntegrationTestEnvironment(contextLocations, jaxrsImplementation, jaxrsClasses, autoDetectJaxrsClasses)
        }

        controller = new JaxrsController()
        controller.jaxrsContext = testEnvironment.jaxrsContext
    }

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

    /**
     * Implementors can define additional Spring application context locations.
     */
    protected String getContextLocations() {
        ''
    }

    /**
     * Returns the JAX-RS implementation to use. Default is 'jersey'.
     */
    protected String getJaxrsImplementation() {
        JaxrsContext.JAXRS_PROVIDER_NAME_JERSEY
    }

    /**
     * Returns the list of JAX-RS classes for testing. Auto-detected classes
     * will be added to this list later.
     */
    protected List getJaxrsClasses() {
        []
    }

    /**
     * Determines whether JAX-RS resources or providers are auto-detected in
     * <code>grails-app/resources</code> or <code>grails-app/providers</code>.
     *
     * @return true is JAX-RS classes should be auto-detected.
     */
    protected boolean isAutoDetectJaxrsClasses() {
        true
    }
}

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

import grails.util.Holders
import org.codehaus.groovy.grails.test.support.GrailsTestAutowirer
import org.grails.jaxrs.JaxrsController
import org.junit.Before
import org.junit.BeforeClass

import javax.servlet.http.HttpServletResponse
/**
 * Base class for JUnit integration testing JAX-RS resources and providers.
 *
 * @author Martin Krasser
 */
abstract class IntegrationTestCase implements JaxRsIntegrationTest {

    private applicationContext = Holders.grailsApplication.mainContext
    private autowirer = new GrailsTestAutowirer(applicationContext)

    def grailsApplication

    static testEnvironment

    def controller

    JaxRsIntegrationTest defaultMixin

    @BeforeClass
    static void setUpBeforeClass() {
        testEnvironment = null
    }

    @Before
    void setUp() {
        autowirer.autowire(this)
        grailsApplication.config.org.grails.jaxrs.dowriter.require.generic.collections = false
        grailsApplication.config.org.grails.jaxrs.doreader.disable = false
        grailsApplication.config.org.grails.jaxrs.dowriter.disable = false

        controller = new JaxrsController()
        defaultMixin = new JaxRsIntegrationTestMixin(controller)

        if (!testEnvironment) {
            testEnvironment = new IntegrationTestEnvironment(getContextLocations(), getJaxrsImplementation(),
                    getJaxrsClasses(), isAutoDetectJaxrsClasses())
        }

        controller.jaxrsContext = testEnvironment.jaxrsContext
    }

    @Override
    void setRequestUrl(String url) {
        defaultMixin.setRequestUrl(url)
    }

    @Override
    void setRequestMethod(String method) {
        defaultMixin.setRequestMethod(method)
    }

    @Override
    void setRequestContent(byte[] content) {
        defaultMixin.setRequestContent(content)
    }

    @Override
    void addRequestHeader(String key, Object value) {
        defaultMixin.addRequestHeader(key, value)
    }

    @Override
    void resetResponse() {
        defaultMixin.resetResponse()
    }

    @Override
    HttpServletResponse getResponse() {
        defaultMixin.response
    }

    @Override
    HttpServletResponse sendRequest(String url, String method, byte[] content) {
        defaultMixin.sendRequest(url, method, content)
    }

    /*
     * overloaded version of sendRequest: since default params are not always compatible with Override, 
     * the method must be redeclared explicitly, see https://jira.codehaus.org/browse/GROOVY-5413
     */
    HttpServletResponse sendRequest(String url, String method) {
        defaultMixin.sendRequest(url, method, ''.bytes)
    }

    @Override
    HttpServletResponse sendRequest(String url, String method, Map<String, Object> headers, byte[] content) {
        defaultMixin.sendRequest(url, method, headers, content)
    }

    /*
     * overloaded version of sendRequest: since default params are not always compatible with Override, 
     * the method must be redeclared explicitly, see https://jira.codehaus.org/browse/GROOVY-5413
     */
    HttpServletResponse sendRequest(String url, String method, Map<String, Object> headers) {
        defaultMixin.sendRequest(url, method, headers, ''.bytes)
    }

    @Override
    String getContextLocations() {
        defaultMixin.contextLocations
    }

    @Override
    String getJaxrsImplementation() {
        defaultMixin.jaxrsImplementation
    }

    @Override
    List getJaxrsClasses() {
        defaultMixin.jaxrsClasses
    }

    @Override
    boolean isAutoDetectJaxrsClasses() {
        defaultMixin.autoDetectJaxrsClasses
    }
}

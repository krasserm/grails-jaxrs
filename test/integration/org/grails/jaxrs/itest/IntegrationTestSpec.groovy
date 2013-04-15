package org.grails.jaxrs.itest

import grails.plugin.spock.IntegrationSpec
import org.grails.jaxrs.JaxrsController
import spock.lang.Shared

import javax.servlet.http.HttpServletResponse

/**
 * @author Noam Y. Tenne
 */
abstract class IntegrationTestSpec extends IntegrationSpec implements JaxRsIntegrationTest {

    static transactional = false

    @Shared
    def grailsApplication

    @Shared
    def testEnvironment

    def controller

    JaxRsIntegrationTest defaultMixin

    def setupSpec() {
        testEnvironment = null
    }

    def setup() {
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
    HttpServletResponse sendRequest(String url, String method, byte[] content = ''.bytes) {
        defaultMixin.sendRequest(url, method, content)
    }

    @Override
    HttpServletResponse sendRequest(String url, String method, Map<String, Object> headers, byte[] content = ''.bytes) {
        defaultMixin.sendRequest(url, method, headers, content)
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

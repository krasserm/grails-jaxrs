package org.grails.jaxrs.itest

import org.grails.jaxrs.JaxrsController
import org.grails.jaxrs.web.JaxrsContext
import org.grails.jaxrs.web.JaxrsUtils

import javax.servlet.http.HttpServletResponse

/**
 * Default and common implementation of the typical integration test functionality
 *
 * @author Noam Y. Tenne
 */
class JaxRsIntegrationTestMixin implements JaxRsIntegrationTest {

    private JaxrsController controller

    JaxRsIntegrationTestMixin(JaxrsController controller) {
        this.controller = controller
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
    String getContextLocations() {
        ''
    }

    /**
     * Returns the JAX-RS implementation to use. Default is 'jersey'.
     */
    String getJaxrsImplementation() {
        JaxrsContext.JAXRS_PROVIDER_NAME_JERSEY
    }

    /**
     * Returns the list of JAX-RS classes for testing. Auto-detected classes
     * will be added to this list later.
     */
    List getJaxrsClasses() {
        []
    }

    /**
     * Determines whether JAX-RS resources or providers are auto-detected in
     * <code>grails-app/resources</code> or <code>grails-app/providers</code>.
     *
     * @return true is JAX-RS classes should be auto-detected.
     */
    boolean isAutoDetectJaxrsClasses() {
        true
    }
}

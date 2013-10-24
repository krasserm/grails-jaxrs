package org.grails.jaxrs.itest

import javax.servlet.http.HttpServletResponse

/**
 * Specifies functionality that should be common to all integration test bases
 *
 * @author Noam Y. Tenne
 */
public interface JaxRsIntegrationTest {

    void setRequestUrl(String url)

    void setRequestMethod(String method)

    void setRequestContent(byte[] content)

    void addRequestHeader(String key, Object value)

    void resetResponse()

    HttpServletResponse getResponse()

    HttpServletResponse sendRequest(String url, String method, byte[] content)

    HttpServletResponse sendRequest(String url, String method, Map<String, Object> headers, byte[] content)

    String getContextLocations()

    String getJaxrsImplementation()

    List getJaxrsClasses()

    boolean isAutoDetectJaxrsClasses()
}
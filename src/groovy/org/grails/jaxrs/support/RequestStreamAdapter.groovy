package org.grails.jaxrs.support

import javax.servlet.AsyncContext
import javax.servlet.DispatcherType
import javax.servlet.ServletException
import javax.servlet.ServletInputStream
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.Part

import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.jaxrs.springframework.mock.web.DelegatingServletInputStream
import org.grails.jaxrs.springframework.mock.web.MockHttpServletRequest

class RequestStreamAdapter extends MockHttpServletRequest {

    InputStream stream

    RequestStreamAdapter(InputStream stream) {
        this.stream = stream
    }

    String getFormat() {
        return getAttribute(GrailsApplicationAttributes.CONTENT_FORMAT)
    }

    @Override
    ServletInputStream getInputStream() {
        if (stream instanceof ServletInputStream) {
            return stream
        } else if (stream) {
            return new DelegatingServletInputStream(stream)
        } else {
            return super.getInputStream()
        }
    }

    /* The following code is taken from MockHttpServletRequest 3.1.0.RC1 */
    //---------------------------------------------------------------------
    // Methods introduced in Servlet 3.0
    //---------------------------------------------------------------------

    AsyncContext getAsyncContext() {
        throw new UnsupportedOperationException()
    }

    DispatcherType getDispatcherType() {
        throw new UnsupportedOperationException()
    }

    boolean isAsyncSupported() {
        throw new UnsupportedOperationException()
    }

    AsyncContext startAsync() {
        throw new UnsupportedOperationException()
    }

    AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) {
        throw new UnsupportedOperationException()
    }

    boolean isAsyncStarted() {
        throw new UnsupportedOperationException()
    }

    boolean authenticate(HttpServletResponse arg0) throws IOException, ServletException {
        throw new UnsupportedOperationException()
    }

    void addPart(Part part) {
        parts.put(part.getName(), part)
    }

    Part getPart(String key) throws IOException, IllegalStateException, ServletException {
        return parts.get(key)
    }

    Collection<Part> getParts() throws IOException, IllegalStateException, ServletException {
        return parts.values()
    }

    void login(String arg0, String arg1) throws ServletException {
        throw new UnsupportedOperationException()
    }

    void logout() throws ServletException {
        throw new UnsupportedOperationException()
    }
}

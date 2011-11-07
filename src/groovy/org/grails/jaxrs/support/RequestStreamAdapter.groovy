package org.grails.jaxrs.support

import java.io.InputStream;
import javax.servlet.ServletInputStream

import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.AsyncContext
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.Part
import javax.servlet.ServletException
import javax.servlet.DispatcherType

class RequestStreamAdapter extends MockHttpServletRequest {

    InputStream stream
    
    String format
    
    RequestStreamAdapter(InputStream stream) {
        this.stream = stream
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

    public AsyncContext getAsyncContext() {
        throw new UnsupportedOperationException();
    }

    public DispatcherType getDispatcherType() {
        throw new UnsupportedOperationException();
    }

    public boolean isAsyncSupported() {
        throw new UnsupportedOperationException();
    }

    public AsyncContext startAsync() {
        throw new UnsupportedOperationException();
    }

    public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) {
        throw new UnsupportedOperationException();
    }

    public boolean isAsyncStarted() {
        throw new UnsupportedOperationException();
    }

    public boolean authenticate(HttpServletResponse arg0) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }
    
    public void addPart(Part part) {
        parts.put(part.getName(), part);
    }

    public Part getPart(String key) throws IOException, IllegalStateException, ServletException {
        return parts.get(key);
    }

    public Collection<Part> getParts() throws IOException, IllegalStateException, ServletException {
        return parts.values();
    }

    public void login(String arg0, String arg1) throws ServletException {
        throw new UnsupportedOperationException();
    }

    public void logout() throws ServletException {
        throw new UnsupportedOperationException();
    }
}

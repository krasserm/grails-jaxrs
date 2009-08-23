<%=packageName ? "package ${packageName}\n\n" : ''%>import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.PUT
import javax.ws.rs.core.Response

@Produces('text/xml')
class ${resourceName}Resource {
    
    def id
    
    @GET
    Response read() {
        def obj = ${resourceName}.get(id)
        if (obj) {
            ok obj
        } else {
            notFound ${resourceName}.class, id
        }
    }
    
    @PUT
    Response update(Map properties) {
        def obj = ${resourceName}.get(id)
        if (obj) {
            obj.properties = properties 
            ok obj
        } else {
            notFound ${resourceName}.class, id
        }
    }
    
    @DELETE
    void delete() {
        def obj = ${resourceName}.get(id)
        if (obj) { 
            obj.delete()
        }
    }
    
}


<%=packageName ? "package ${packageName}\n\n" : ''%>import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.PUT
import javax.ws.rs.core.Response

import org.grails.jaxrs.provider.DomainObjectNotFoundException

@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class ${resourceName}Resource {
    
    def id
    
    @GET
    Response read() {
        def obj = ${resourceName}.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(${resourceName}.class, id)
        }
        ok obj
    }
    
    @PUT
    Response update(Map properties) {
        def obj = ${resourceName}.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(${resourceName}.class, id)
        }
        obj.properties = properties 
        ok obj
    }
    
    @DELETE
    void delete() {
        def obj = ${resourceName}.get(id)
        if (obj) { 
            obj.delete()
        }
    }
    
}


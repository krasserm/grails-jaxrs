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

    def ${resourceProp}ResourceService
    def id

    @GET
    Response read() {
        ok ${resourceProp}ResourceService.read(id)
    }

    @PUT
    Response update(${resourceName} dto) {
        dto.id = id
        ok ${resourceProp}ResourceService.update(dto)
    }

    @DELETE
    void delete() {
        ${resourceProp}ResourceService.delete(id)
    }
}

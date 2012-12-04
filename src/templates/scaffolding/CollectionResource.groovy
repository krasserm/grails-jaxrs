<%=packageName ? "package ${packageName}\n\n" : ''%>import static org.grails.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.POST
import javax.ws.rs.core.Response

@Path('/api/${resourcePath}')
@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class ${resourceName}CollectionResource {

    def ${resourceProp}ResourceService

    @POST
    Response create(${resourceName} dto) {
        created ${resourceProp}ResourceService.create(dto)
    }

    @GET
    Response readAll() {
        ok ${resourceProp}ResourceService.readAll()
    }

    @Path('/{id}')
    ${resourceName}Resource getResource(@PathParam('id') Long id) {
        new ${resourceName}Resource(${resourceProp}ResourceService: ${resourceProp}ResourceService, id:id)
    }
}

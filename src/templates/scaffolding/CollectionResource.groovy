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

    @POST
    Response create(${resourceName} dto) {
        created dto.save()
    }

    @GET
    Response readAll() {
        ok ${resourceName}.findAll()
    }
    
    @Path('/{id}')
    ${resourceName}Resource getResource(@PathParam('id') String id) {
        new ${resourceName}Resource(id:id)
    }
        
}

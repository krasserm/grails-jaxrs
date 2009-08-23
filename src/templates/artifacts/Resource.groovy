@artifact.package@import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces


@Path('/@resource.path@')
class @artifact.name@ {

    @GET
    @Produces('text/plain')
    String get@resource.name@Representation() {
        '@resource.name@'
    }
    
}

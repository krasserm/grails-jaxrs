import javax.ws.rs.Consumesimport javax.ws.rs.POSTimport javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

import grails.converters.JSON
import grails.converters.XMLimport org.grails.jaxrs.provider.DomainObjectNotFoundException
@Path('/api/test')
class TestResource {

    @GET     @Produces(['application/xml'])    Person getTest() {        return new Person(age:33, name:'mike')        //throw new DomainObjectNotFoundException(String.class, 1)    }}

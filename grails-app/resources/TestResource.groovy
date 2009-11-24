import javax.ws.rs.Consumesimport javax.ws.rs.POSTimport grails.converters.XMLimport javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

import grails.converters.JSON

@Path('/test')
class TestResource {

//    @GET 
//    @Produces(['application/json', 'text/x-json'])
//    JSON getTestJson() { 
//        new Person(name:'martin') as JSON
//    }
    
//    @POST//    @Consumes(['text/x-json', 'text/xml'])//    @Produces('text/x-json')//    JSON getTestJson(Map params) {//        println params//        def p = new Person(params)//        p.name = p.name + 'xxx'//        p as JSON//    }        @GET 
    @Produces(['application/xml', 'application/json'])
    Object getTestXml() { 
        new Person(name:'martin')
    }
    
//    @GET @Produces('*/*')
//    JSON getTestDefault() { 
//        getTestJson()
//    }
      
}

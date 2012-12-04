/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.jaxrs.response

import javax.ws.rs.core.Response
import javax.ws.rs.core.UriBuilder

/**
 * Factory methods for JAX-RS response objects. This class is used by JAX-RS
 * resource implementations to create XML responses from Grails domain objects.
 * Resources generated with the command
 *
 * <pre>
 * grails generate-resources &lt;domain-object-class&gt;
 * </pre>
 *
 * make use of these factory methods.
 *
 * @author Martin Krasser
 */
class Responses {

     /**
      * Creates a response to a POST operation with status code 201,
      * an XML representation of the created resource as response entity
      * and a Location-header with the URL of the created resource.
      *
      * @param resource a Grails domain object.
      * @return JAX-RS response.
      */     static Response created(resource) {
         URI uri = UriBuilder.fromPath(resource.id as String).build()
         Response.created(uri).entity(resource).build()
     }
     /**
      * Creates a response to a GET operation with status code 200 and
      * an XML representation of the given resource as response entity.
      *
      * @param resource a Grails domain object.
      * @return JAX-RS response.
      */
     static Response ok(resource) {
         Response.ok(resource).build()
     }
}

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
 
import static javax.ws.rs.core.Response.Status.NOT_FOUND

import grails.converters.* 

import javax.ws.rs.core.Response
import javax.ws.rs.core.UriBuilder

/**
 * @author Martin Krasser
 */
class Responses {
     static Response created(def resource) {
         URI uri = UriBuilder.fromPath(resource.id as String).build()
         Response.created(uri).entity(resource as XML).build()
     }
          static Response ok(def resource) {
         Response.ok(resource as XML).build()
     }
     
     static Response notFound(def clazz, def id) {
         Response.status(NOT_FOUND).entity(notFoundMessage(clazz, id)).build()
     }

     private static String notFoundMessage(def clazz, def id) {         "<error>${clazz.simpleName} with id $id not found</error>"     }     
}

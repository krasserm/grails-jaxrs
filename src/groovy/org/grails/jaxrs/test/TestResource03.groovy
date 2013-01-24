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
package org.grails.jaxrs.test

import grails.converters.JSON

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

/**
 * @author Martin Krasser
 */
@Path('/test/03')
class TestResource03 {

    @POST
    @Consumes('application/json')
    @Produces('application/json')
    JSON testPerson(Map params) {
        def person = new TestPerson(params)
        person.name = person.name.reverse()
        person.age = person.age + 1
        return person as JSON
    }
}

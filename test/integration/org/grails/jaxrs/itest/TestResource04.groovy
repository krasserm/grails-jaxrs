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
package org.grails.jaxrs.itest

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

/**
 * @author Martin Krasser
 */
@Path('/test/04')
class TestResource04 {

    @POST
    @Path('/single')
    @Consumes(['application/json', 'application/xml'])
    @Produces(['application/json', 'application/xml'])
    TestPerson testPostPersonSingle(TestPerson person) {
        person.name  = person.name.reverse()
        person.age = person.age + 1
        return person
    }

    @GET
    @Path('/multi/generic')
    @Produces(['application/json', 'application/xml'])
    Collection<TestPerson> testGetPersonsMultiGeneric() {
        createPersonList()
    }

    @GET
    @Path('/multi/raw')
    @Produces(['application/json', 'application/xml'])
    Collection testGetPersonsMultiRaw() {
        createPersonList()
    }

    @GET
    @Path('/multi/object')
    @Produces(['application/json', 'application/xml'])
    def testGetPersonsMultiObject() {
        createPersonList()
    }

    private createPersonList() {
        [new TestPerson(name:'n1', age:1),
         new TestPerson(name:'n2', age:2)]
    }
}

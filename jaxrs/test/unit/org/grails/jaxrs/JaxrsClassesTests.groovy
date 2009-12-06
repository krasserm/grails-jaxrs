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
package org.grails.jaxrs

import java.io.ByteArrayInputStream
import javax.ws.rs.Path
import javax.ws.rs.GET
import javax.ws.rs.ext.MessageBodyReader
import javax.ws.rs.ext.Provider
import grails.test.*

/**
 * @author Martin Krasser
 */
class JaxrsClassesTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testIsJaxrsResource() {        assertTrue(JaxrsClasses.isJaxrsResource(TestA.class))
        assertTrue(JaxrsClasses.isJaxrsResource(TestB.class))
        assertTrue(JaxrsClasses.isJaxrsResource(TestC.class))
        assertFalse(JaxrsClasses.isJaxrsResource(TestD.class))
        assertFalse(JaxrsClasses.isJaxrsResource(TestE.class))
    }
    
    void testIsJaxrsResourceInherit() {
        assertTrue(JaxrsClasses.isJaxrsResource(TestH1B.class))
        assertFalse(JaxrsClasses.isJaxrsResource(TestH2B.class))
        assertTrue(JaxrsClasses.isJaxrsResource(TestH1B.class))
    }
    
}

@Path('/a') class TestA { @GET String a() {'a'} }@Path('/b') class TestB { String b() {'b'} }class TestC { @GET String c() {'c'} }class TestD { String d() {'d'} }
abstract class TestE implements MessageBodyReader { @GET String e() {'e'} }

@Path('/a') class TestH1A {}
class TestH1B extends TestH1A {}

class TestH2A {}
class TestH2B extends TestH2A {}

@Path('/a') interface TestH3A {}
class TestH3B implements TestH3A {}

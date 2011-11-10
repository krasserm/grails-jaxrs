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
import grails.test.mixin.TestFor
import grails.test.mixin.support.GrailsUnitTestMixin

import org.grails.jaxrs.test.*

/**
 * @author Martin Krasser
 */
//class JaxrsClassesTests extends GrailsUnitTestCase {
@TestMixin(GrailsUnitTestMixin)
class JaxrsClassesTests {

    void testIsJaxrsResource() {
	assertTrue(JaxrsClasses.isJaxrsResource(TestA.class))
        assertTrue(JaxrsClasses.isJaxrsResource(TestB.class))
        assertTrue(JaxrsClasses.isJaxrsResource(TestC.class))
        assertFalse(JaxrsClasses.isJaxrsResource(TestD.class))
        assertFalse(JaxrsClasses.isJaxrsResource(TestE.class))
    }
    
    void testIsJaxrsResourceInherit() {
        assertTrue(JaxrsClasses.isJaxrsResource(TestH1B.class))
        assertFalse(JaxrsClasses.isJaxrsResource(TestH2B.class))
        assertTrue(JaxrsClasses.isJaxrsResource(TestH3B.class))
    }
}
    


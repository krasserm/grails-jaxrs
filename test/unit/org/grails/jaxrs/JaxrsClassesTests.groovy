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

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

import org.grails.jaxrs.test.TestA
import org.grails.jaxrs.test.TestB
import org.grails.jaxrs.test.TestC
import org.grails.jaxrs.test.TestD
import org.grails.jaxrs.test.TestE
import org.grails.jaxrs.test.TestH1B
import org.grails.jaxrs.test.TestH2B
import org.grails.jaxrs.test.TestH3B

/**
 * @author Martin Krasser
 */
@TestMixin(GrailsUnitTestMixin)
class JaxrsClassesTests {

    void testIsJaxrsResource() {
        assertTrue(JaxrsClasses.isJaxrsResource(TestA))
        assertTrue(JaxrsClasses.isJaxrsResource(TestB))
        assertTrue(JaxrsClasses.isJaxrsResource(TestC))
        assertFalse(JaxrsClasses.isJaxrsResource(TestD))
        assertFalse(JaxrsClasses.isJaxrsResource(TestE))
    }

    void testIsJaxrsResourceInherit() {
        assertTrue(JaxrsClasses.isJaxrsResource(TestH1B))
        assertFalse(JaxrsClasses.isJaxrsResource(TestH2B))
        assertTrue(JaxrsClasses.isJaxrsResource(TestH3B))
    }
}

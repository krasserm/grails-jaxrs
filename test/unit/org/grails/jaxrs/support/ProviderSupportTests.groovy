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
package org.grails.jaxrs.support

import static org.grails.jaxrs.support.ConverterUtils.xmlToMap

import java.io.ByteArrayInputStream
import javax.ws.rs.core.MultivaluedMap

import grails.test.*

/**
 * @author Martin Krasser
 */
class ProviderSupportTests extends GrailsUnitTestCase {

    ProviderSupport providerA
    ProviderSupport providerB
    ProviderSupport providerC
         protected void setUp() {
        super.setUp()
        providerA = new TestProviderA() 
        providerB = new TestProviderB() 
        providerC = new TestProviderC() 
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testProviderA() {
        assertTrue(providerA.isSupported(TestX.class, null, null, null))
        assertTrue(providerA.isSupported(TestY.class, null, null, null))
        assertFalse(providerA.isSupported(ArrayList.class, null, null, null))
    }

    void testProviderB() {
        assertFalse(providerB.isSupported(TestX.class, null, null, null))
        assertTrue(providerB.isSupported(TestY.class, null, null, null))
        assertFalse(providerB.isSupported(ArrayList.class, null, null, null))
    }

    void testProviderC() {
        assertFalse(providerC.isSupported(TestX.class, null, null, null))
        assertFalse(providerC.isSupported(TestY.class, null, null, null))
        assertTrue(providerC.isSupported(ArrayList.class, null, null, null))
    }

}
class TestX {}class TestY extends TestX {}class TestProviderBase<T> extends MessageBodyWriterSupport<T> {    protected void writeTo(T t, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) {        // noop    }}class TestProviderA extends TestProviderBase<TestX> {}
class TestProviderB extends TestProviderBase<TestY> {}
class TestProviderC extends TestProviderBase<List<TestX>> {}


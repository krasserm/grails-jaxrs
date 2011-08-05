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

import static org.grails.jaxrs.support.ConverterUtils.idFromMap
import static org.grails.jaxrs.support.ConverterUtils.xmlToMap

import java.io.ByteArrayInputStreamimport grails.test.*

/**
 * @author Martin Krasser
 */
class ConverterUtilsTests extends GrailsUnitTestCase {
    private static final String encoding = 'UTF-8'      
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testXmlToMap() {
        def xml = "<x id=\"1\"><y>2</y><z>3</z></x>"
        def map = xmlToMap(new ByteArrayInputStream(xml.getBytes(encoding)), encoding)        assertEquals('1', map.id) 
        assertEquals('2', map.y)         assertEquals('3', map.z)     }
    
    void testIdFromMapWithNumericId() {
        def xml = "<x id=\"1\"><y>2</y><z>3</z></x>"
        def map = xmlToMap(new ByteArrayInputStream(xml.getBytes(encoding)), encoding)
        assertEquals(new Long(1), idFromMap(map)) 
    }
    
    void testIdFromMapWithNonNumericId() {
        def xml = "<x id=\"a\"><y>2</y><z>3</z></x>"
        def map = xmlToMap(new ByteArrayInputStream(xml.getBytes(encoding)), encoding)
        assertEquals('a', idFromMap(map)) 
    }
    
}

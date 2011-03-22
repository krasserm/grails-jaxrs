package org.grails.jaxrs
/*
 * Copyright 2009 - 2011 the original author or authors.
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

import static JaxrsControllerIntegrationTests.jaxrsClasses

import java.util.List;
 
import org.grails.jaxrs.provider.DomainObjectReader
import org.grails.jaxrs.provider.DomainObjectWriter
import org.grails.jaxrs.provider.JSONWriter
import org.grails.jaxrs.provider.JSONReader
import org.grails.jaxrs.test.integration.CustomRequestEntityReader
import org.grails.jaxrs.test.integration.CustomResponseEntityWriter
import org.grails.jaxrs.test.integration.TestResource01
import org.grails.jaxrs.test.integration.TestResource02
import org.grails.jaxrs.test.integration.TestResource03
import org.grails.jaxrs.test.integration.TestResource04
import org.grails.jaxrs.test.integration.TestResource05

/**
 * @author Martin Krasser
 */
public class RestletControllerIntegrationTests extends JaxrsControllerIntegrationTests {

    @Override
    protected List getJaxrsClasses() {
        [TestResource01.class, 
         TestResource02.class, 
         TestResource03.class, 
         TestResource04.class, 
         TestResource05.class, 
         CustomRequestEntityReader.class, 
         CustomResponseEntityWriter.class,
         JSONReader.class,
         JSONWriter.class,
         DomainObjectReader.class,
         DomainObjectWriter.class]    
    }
        
}

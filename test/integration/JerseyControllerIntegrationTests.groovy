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

import static JaxrsControllerIntegrationTests.jaxrsClasses
 
import org.grails.jaxrs.web.IntegrationTestEnvironment

// EXPERIMENTAL
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


/**
 * @author Martin Krasser
 */
public class JerseyControllerIntegrationTests extends JaxrsControllerIntegrationTests {

    static transactional = false
    static environment  
    
    void setUp() {
        if (!environment) {
            environment = new IntegrationTestEnvironment('context-integration.xml', 'jersey', jaxrsClasses)
        }
        super.setUp()
        controller = new JaxrsController()
        controller.jaxrsContext = environment.jaxrsContext 
    }
    
}

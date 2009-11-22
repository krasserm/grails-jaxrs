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
 
/**
 * @author Martin Krasser
 */
public class JerseyControllerIntegrationTests extends JaxrsControllerIntegrationTests {

    static environment = new IntegrationTestEnvironment('context-integration.xml', 'jersey', jaxrsClasses) 
    static transactional = false
    
    def controller
    
    void setUp() {
        controller = new JaxrsController()
        controller.jaxrsContext = environment.jaxrsContext 
    }
    
    void testGetTest01() {
        testGetTest01(controller)
    }
    
    void testPostTest02() {
        testPostTest02(controller)
    }
    
    void testPostTest03() {
        testPostTest03(controller)
    }
    
}

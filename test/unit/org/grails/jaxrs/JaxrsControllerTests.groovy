package org.grails.jaxrs
/* * Copyright 2009 - 2011 the original author or authors. * * Licensed under the Apache License, Version 2.0 (the "License"); * you may not use this file except in compliance with the License. * You may obtain a copy of the License at * *     http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the License. */import grails.test.ControllerUnitTestCase

import org.grails.jaxrs.web.JaxrsUtils
import org.grails.jaxrs.web.UnitTestEnvironment

/** * @author Martin Krasser */class JaxrsControllerTests extends ControllerUnitTestCase {
    static environment = new UnitTestEnvironment()    JaxrsControllerTests() {        super(JaxrsController)    }
    void setUp() {        super.setUp()        controller.jaxrsContext = environment.jaxrsContext    }

    void testGetTest() {
        controller.request.method = 'GET'        JaxrsUtils.setRequestUriAttribute(controller.request, '/test')        controller.handle()        assertEquals('/test', controller.response.contentAsString.trim())    }
}

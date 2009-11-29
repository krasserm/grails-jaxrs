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
package org.grails.jaxrs.test.integration;

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication;

/**
 * @author Martin Krasser
 */
public class TestGrailsApplication extends DefaultGrailsApplication {

    private static TestGrailsApplication instance = new TestGrailsApplication();
    
    public static TestGrailsApplication getInstance() {
        return instance;
    }
    
    @Override
    public Object invokeMethod(String methodName, Object args) {
        if (methodName.equals("isDomainClass")) {
            Object[] arguments = (Object[])args;
            return arguments[0].equals(TestPerson.class);
        } else {
            return super.invokeMethod(methodName, args);
        }
    }
    
}

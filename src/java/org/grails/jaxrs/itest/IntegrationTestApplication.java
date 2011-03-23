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
package org.grails.jaxrs.itest;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsApplication;

/**
 * @author Martin Krasser
 */
public class IntegrationTestApplication extends DefaultGrailsApplication {

    private static IntegrationTestApplication instance = new IntegrationTestApplication();
    
    private List<Class<?>> domainClasses = new ArrayList<Class<?>>();
    
    private GrailsApplication pluginApplication;
    
    public static IntegrationTestApplication getInstance() {
        return instance;
    }

    public List<Class<?>> getDomainClasses() {
        return domainClasses;
    }
    
    public GrailsApplication getPluginApplication() {
        return pluginApplication;
    }

    public void setPluginApplication(GrailsApplication pluginApplication) {
        this.pluginApplication = pluginApplication;
    }

    @Override
    public Object invokeMethod(String methodName, Object args) {
        if (methodName.equals("isDomainClass")) {
            Object[] arguments = (Object[])args;
            return domainClasses.contains(arguments[0]);
        } else {
            return super.invokeMethod(methodName, args);
        }
    }
    
}

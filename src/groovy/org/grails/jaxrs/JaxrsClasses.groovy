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

/**
 * Provides condition methods to determine whether a given class is a valid
 * JAX-RS class.
 * 
 * @author Martin Krasser
 */
class JaxrsClasses {

    static def jaxrsAnnotationCondition = {annotation -> annotation.toString().contains('javax.ws.rs')}
    static def jaxrsClassCondition      = {clazz      -> clazz.declaredAnnotations.any jaxrsAnnotationCondition}
    static def jaxrsMethodCondition     = {method     -> method.declaredAnnotations.any jaxrsAnnotationCondition}
    static def jaxrsMethodsCondition    = {clazz      -> clazz.declaredMethods.any jaxrsMethodCondition}

    /**
     * Returns <code>true</code> if the given class is a valid JAX-RS class,
     * <code>false</code> otherwise. A class in considered a JAX-RS class if
     * there is either a JAX-RS annotation present on class-level or on 
     * method-level.
     * 
     * @param clazz class to be checked.
     * @return 
     */
    static boolean isJaxrsResource(Class clazz) {
        jaxrsClassCondition(clazz) || jaxrsMethodsCondition(clazz)
    }
    
}

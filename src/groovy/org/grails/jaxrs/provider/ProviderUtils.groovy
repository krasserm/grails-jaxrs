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
package org.grails.jaxrs.provider

import javax.ws.rs.core.MediaType
/**
 * @author Martin Krasser
 */
class ProviderUtils {

     /**
      * Checks <code>mediaType</code> for XML compatibility
      */
     static boolean isXmlType(MediaType mediaType) {
         mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE) ||
         mediaType.isCompatible(MediaType.TEXT_XML_TYPE)
     }
     
     /**
      * Checks <code>mediaType</code> for JSON compatibility
      */
     static boolean isJsonType(MediaType mediaType) {
         mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE) ||
         mediaType.isCompatible(new MediaType('text', 'x-json'))
     }

}

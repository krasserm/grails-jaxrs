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

import java.util.Mapimport org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.servlet.mvc.ParameterCreationListener
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import grails.converters.XML
import org.apache.commons.logging.*
import groovy.util.slurpersupport.GPathResult

/**
 * @author Martin Krasser
 */
class ConverterUtils {
     
     static final LOG = LogFactory.getLog(ConverterUtils.class)

     static String getDefaultEncoding(GrailsApplication application) {
         def encoding = application.config.grails.converters.encoding
         // TODO: use platform encoding if application doesn't provide
         encoding ? encoding : 'UTF-8' 
     }
     
     static jsonToMap(InputStream input, String encoding) {
         throw new UnsupportedOperationException('not implemented')
     }
     
     static Map xmlToMap(InputStream input, String encoding) {
         def map = [:]
         try {
             GPathResult xml = XML.parse(input, encoding)
             def name =  xml.name()
             def id = xml.@id.text()
             if (id) {
                 map['id'] = id
             }
             populateParamsFromXML(xml, map)
             def target = [:]
             createFlattenedKeys(map, map, target)
             for(entry in target) {
                 if(!map[entry.key]) {
                     map[entry.key] = entry.value
                 }
             }
         } catch (Exception e) {
             LOG.error('Error parsing incoming XML request', e)
         }
         map
     }
     
     private static populateParamsFromXML(xml, map) {
         for(child in xml.children()) {
             if (child.@id.text()) {
                 map["${child.name()}.id"] = child.@id.text()
                 def childMap = [:]
                 map[child.name()] = childMap
                 populateParamsFromXML(child, childMap)
             } else {
                 map[child.name()] = child.text()
             }
         }
     }
    
     private static createFlattenedKeys(Map root, Map current, Map target, prefix = '') {
         for (entry in current) {
             if (entry.value instanceof Map) {
                 createFlattenedKeys(root,entry.value, target, "${entry.key}.")
             } else if (prefix) {
                 target["${prefix}${entry.key}"] = entry.value
             }
         }
     }
    
}

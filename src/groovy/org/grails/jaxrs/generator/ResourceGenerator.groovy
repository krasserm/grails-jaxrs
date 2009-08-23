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
package org.grails.jaxrs.generator

import grails.util.GrailsNameUtils
import groovy.text.SimpleTemplateEngine
import org.apache.commons.logging.Logimport org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.springframework.core.io.FileSystemResourceimport org.springframework.core.io.ClassPathResource
/**
 * @author Martin Krasser
 */
class ResourceGenerator {

     private static final Log LOG = LogFactory.getLog(ResourceGenerator.class)
     
     def engine
     def pluginDir
     
     boolean overwrite = false
     
     ResourceGenerator() {
         engine = new SimpleTemplateEngine()
     }

     void generateResources(GrailsDomainClass domainClass, String destdir) {
         if (!destdir) {
             throw new IllegalArgumentException("Argument [destdir] not specified")
         }
    
         if (domainClass) {
             def fullName = domainClass.fullName
             def pos = fullName.lastIndexOf('.')
             def pkg = ''
             if (pos != -1) {
                 pkg = fullName[0..pos]
             }
             def path = "${destdir}/grails-app/resources/${pkg.replace('.' as char, '/' as char)}"
    
             generateCollectionResource(domainClass, path)
             generateResource(domainClass, path)
         }
     }
     
     void generateCollectionResource(GrailsDomainClass domainClass, String path) {
         def destFile = new File("${path}${domainClass.shortName}CollectionResource.groovy")
         if (canWrite(destFile)) {
             destFile.parentFile.mkdirs()
             destFile.withWriter {w ->
                 generateCollectionResource(domainClass, w)
             }
             LOG.info("Collection resource generated at ${destFile}")
         }
     }
     
     void generateResource(GrailsDomainClass domainClass, String path) {
         def destFile = new File("${path}${domainClass.shortName}Resource.groovy")
         if (canWrite(destFile)) {
             destFile.parentFile.mkdirs()
             destFile.withWriter {w ->
                 generateResource(domainClass, w)
             }
             LOG.info("Resource generated at ${destFile}")
         }
     }
     
     void generateCollectionResource(GrailsDomainClass domainClass, Writer out) {
         def templateText = getResourceTemplateText("CollectionResource.groovy")
         def binding = [
             packageName : domainClass.packageName,
             resourceName : domainClass.shortName,
             resourcePath : GrailsNameUtils.getPropertyName(domainClass.shortName)
         ]
         engine.createTemplate(templateText).make(binding).writeTo(out)
     }
    
     void generateResource(GrailsDomainClass domainClass, Writer out) {
         def templateText = getResourceTemplateText("Resource.groovy")
         def binding = [
             packageName : domainClass.packageName,
             resourceName : domainClass.shortName,
             resourcePath : GrailsNameUtils.getPropertyName(domainClass.shortName)
         ]
         engine.createTemplate(templateText).make(binding).writeTo(out)
     }
    
     private canWrite(testFile) {
         if (!overwrite && testFile.exists()) {
             def ant = new AntBuilder()
             ant.input(message: "File ${testFile} already exists. Overwrite?", "y,n,a", addproperty: "overwrite.${testFile.name}")
             overwrite = (ant.antProject.properties."overwrite.${testFile.name}" == "a") ? true : overwrite
             return overwrite || ((ant.antProject.properties."overwrite.${testFile.name}" == "y") ? true : false)
         }
         return true
     }

     private getResourceTemplateText(String template) {
         new FileSystemResource("${pluginDir}/src/templates/scaffolding/${template}").inputStream.getText()
     }

}

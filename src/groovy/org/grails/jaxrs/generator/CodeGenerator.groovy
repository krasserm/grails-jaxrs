/*
 * Copyright 2009-2011 the original author or authors.
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

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.springframework.core.io.FileSystemResource

/**
 * Code generator that generates resource classes and service classes for
 * Grails domain objects. For each domain class X on service class two
 * resource classes are created, XResource for operating on a single
 * X and XCollectionResource for operating on an X collection. Both resource
 * classes delegate to the service class.
 *
 * @author Martin Krasser
 */
class CodeGenerator {

     private static final Log LOG = LogFactory.getLog(CodeGenerator)

     def engine = new SimpleTemplateEngine()
     def pluginDir

     boolean overwrite = false

     /**
      * Generates JAX-RS resource and service classes for the given domain class
      * to the given destination directory.
      *
      * @param domainClass Grails domain class.
      * @param destdir destination directory.
      */
     void generate(GrailsDomainClass domainClass, String destdir) {
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

             def pkgPath = pkg.replace('.' as char, '/' as char)

             def resourcePath = "${destdir}/grails-app/resources/${pkgPath}"
             generateCollectionResource(domainClass, resourcePath)
             generateResource(domainClass, resourcePath)

             def servicePath = "${destdir}/grails-app/services/${pkgPath}"
             generateService(domainClass, servicePath)
         }
     }

     protected void generateCollectionResource(GrailsDomainClass domainClass, String path) {
         def destFile = new File("${path}${domainClass.shortName}CollectionResource.groovy")
         if (canWrite(destFile)) {
             destFile.parentFile.mkdirs()
             destFile.withWriter {w ->
                 generateCollectionResource(domainClass, w)
             }
             LOG.info("Collection resource generated at ${destFile}")
         }
     }

     protected void generateResource(GrailsDomainClass domainClass, String path) {
         def destFile = new File("${path}${domainClass.shortName}Resource.groovy")
         if (canWrite(destFile)) {
             destFile.parentFile.mkdirs()
             destFile.withWriter {w ->
                 generateResource(domainClass, w)
             }
             LOG.info("Resource generated at ${destFile}")
         }
     }

     protected void generateService(GrailsDomainClass domainClass, String path) {
         def destFile = new File("${path}${domainClass.shortName}ResourceService.groovy")
         if (canWrite(destFile)) {
             destFile.parentFile.mkdirs()
             destFile.withWriter {w ->
                 generateService(domainClass, w)
             }
             LOG.info("Service generated at ${destFile}")
         }
     }

     protected void generateCollectionResource(GrailsDomainClass domainClass, Writer out) {
         def templateText = getResourceTemplateText("CollectionResource.groovy")
         def propertyName = GrailsNameUtils.getPropertyName(domainClass.shortName)
         def binding = [
             packageName : domainClass.packageName,
             resourceName : domainClass.shortName,
             resourceProp : propertyName,
             resourcePath : propertyName
         ]
         engine.createTemplate(templateText).make(binding).writeTo(out)
     }

     protected void generateResource(GrailsDomainClass domainClass, Writer out) {
         def templateText = getResourceTemplateText("Resource.groovy")
         def propertyName = GrailsNameUtils.getPropertyName(domainClass.shortName)
         def binding = [
             packageName : domainClass.packageName,
             resourceName : domainClass.shortName,
             resourceProp : propertyName,
             resourcePath : propertyName
         ]
         engine.createTemplate(templateText).make(binding).writeTo(out)
     }

     protected void generateService(GrailsDomainClass domainClass, Writer out) {
         def templateText = getResourceTemplateText("ResourceService.groovy")
         def binding = [
             packageName : domainClass.packageName,
             resourceName : domainClass.shortName,
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

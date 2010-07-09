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

import grails.util.GrailsNameUtils

import org.codehaus.groovy.grails.scaffolding.*

/**
 * @author Martin Krasser
 */

includeTargets << grailsScript("_GrailsBootstrap")
includeTargets << grailsScript("_GrailsCreateArtifacts")

generateForName = null
generateResources = true


target ('default': "Generates the JAX-RS resources for domain classes") {
    depends( checkVersion, parseArguments, packageApp )

    promptForName(type: "Domain Class")

    try {
        def name = argsMap["params"][0]
        if (!name || name == "*") {
            generateForAllResources()
        } else {
            generateForName = name
            generateForOneResource()
        }
    } catch(Exception e) {
        logError("Error running generate-resources", e)
        exit(1)
    }
 
}

target(generateForOneResource: "Generates JAX-RS resources for one domain class.") {
    depends(loadApp)
    
    bootstrap()
    
    def name = generateForName
    name = name.indexOf('.') > -1 ? name : GrailsNameUtils.getClassNameRepresentation(name)
    def domainClass = grailsApp.getDomainClass(name)

    if(!domainClass) {
        println "Domain class not found in grails-app/domain, trying hibernate mapped classes..."
        domainClass = grailsApp.getDomainClass(name)
    }

    if(domainClass) {
        generateForDomainClass(domainClass)
        event("StatusFinal", ["Finished generation for domain class ${domainClass.fullName}"])
    }
    else {
        event("StatusFinal", ["No domain class found for name ${name}. Please try again and enter a valid domain class name"])
    }
}

target(generateForAllResources: "Generates JAX-RS resources for all domain classes.") {
    depends(loadApp)

    bootstrap()
    
    def domainClasses = grailsApp.domainClasses

    if (!domainClasses) {
        println "No domain classes found in grails-app/domain, trying hibernate mapped classes..."
        domainClasses = grailsApp.domainClasses
    }

    if (domainClasses) {
        domainClasses.each { domainClass ->
            generateForDomainClass(domainClass)
        }
        event("StatusFinal", ["Finished generation for domain classes"])
    }
    else {
        event("StatusFinal", ["No domain classes found"])
    }
}

def generateForDomainClass(domainClass) {
    def resourceGenerator = appCtx.getBean('org.grails.jaxrs.generator.ResourceGenerator')
    resourceGenerator.pluginDir = jaxrsPluginDir
    if (generateResources) {
        event("StatusUpdate", ["Generating JAX-RS resources for domain class ${domainClass.fullName}"])
        resourceGenerator.generateResources(domainClass, basedir)
        event("GenerateResourcesEnd", [domainClass.fullName])
    }
}


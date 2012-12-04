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

import grails.util.GrailsNameUtils

import org.codehaus.groovy.grails.scaffolding.*

/**
 * @author Martin Krasser
 */

includeTargets << grailsScript("_GrailsBootstrap")
includeTargets << grailsScript("_GrailsCreateArtifacts")

artifactDescription = null
generateForName = null

target(generateResources: "Generates JAX-RS resource and CRUD service classes for domain classes") {
    depends( checkVersion, parseArguments, packageApp )

    promptForName(type: "Domain Class")

    try {
        def name = argsMap["params"][0]
        if (!name || name == "*") {
            generateForAllDomainClasses()
        } else {
            generateForName = name
            generateForOneDomainClass()
        }
    } catch(Exception e) {
        logError("Error running generate-resources", e)
        exit(1)
    }
}

target(generateForOneDomainClass: "Generates JAX-RS resource and CRUD service classes for one domain class.") {
    depends(loadApp, bootstrap)

    def name = generateForName
    name = name.indexOf('.') > -1 ? name : GrailsNameUtils.getClassNameRepresentation(name)
    def domainClass = grailsApp.getDomainClass(name)

    if(!domainClass) {
        println "Domain class not found in grails-app/domain, trying hibernate mapped classes..."
        domainClass = grailsApp.getDomainClass(name)
    }

    if(domainClass) {
        generateForDomainClass(domainClass)
        event("StatusFinal", ["Finished generation of JAX-RS resource and CRUD service classes for domain class ${domainClass.fullName}"])
    }
    else {
        event("StatusFinal", ["No domain class found for name ${name}. Please try again and enter a valid domain class name"])
    }
}

target(generateForAllDomainClasses: "Generates JAX-RS resource and CRUD service classes for all domain classes.") {
    depends(loadApp, bootstrap)

    def domainClasses = grailsApp.domainClasses

    if (!domainClasses) {
        println "No domain classes found in grails-app/domain, trying hibernate mapped classes..."
        domainClasses = grailsApp.domainClasses
    }

    if (domainClasses) {
        domainClasses.each { domainClass ->
            generateForDomainClass(domainClass)
        }
        event("StatusFinal", ["Finished generation of JAX-RS resource and CRUD service classes for domain classes"])
    }
    else {
        event("StatusFinal", ["No domain classes found"])
    }
}

def generateForDomainClass(domainClass) {
    def codeGenerator = appCtx.getBean('org.grails.jaxrs.generator.CodeGenerator')
    codeGenerator.pluginDir = jaxrsPluginDir
    event("StatusUpdate", ["Generating JAX-RS resource and CRUD service classes for domain class ${domainClass.fullName}"])
    codeGenerator.generate(domainClass, basedir)
    event("GenerateResourcesEnd", [domainClass.fullName])
}

setDefaultTarget generateResources

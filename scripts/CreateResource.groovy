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

/**
 * @author Martin Krasser
 */

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsCreateArtifacts")

target(createResource: "Creates a new JAX-RS resource class") {

    depends(checkVersion, parseArguments)

    def type = "Resource"
    promptForName(type: type)

    def name = argsMap["params"][0]
    createArtifact(name: name, suffix: type, type: type, path: "grails-app/resources")
    createUnitTest(name: name, suffix: type)
}

setDefaultTarget createResource

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

grails.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        grailsHome()
        mavenCentral()
        mavenRepo "http://download.java.net/maven/2"
        mavenRepo "http://download.java.net/maven/1"
        mavenRepo "http://maven.restlet.org"
    }  

    dependencies {
        compile('asm:asm:3.3',
                'org.restlet.gae:org.restlet:2.0-RC3',
                'org.restlet.gae:org.restlet.ext.servlet:2.0-RC3',
                // A modified version (with removed META-INF/services/javax.ws.rs.ext.RuntimeDelegate)
                // is contained in the project's lib folder. This is needed because of a bug described 
                // at http://restlet.tigris.org/issues/show_bug.cgi?id=1251
                //'org.restlet.gae:org.restlet.ext.jaxrs:2.0-RC3',
                'org.restlet.gae:org.restlet.ext.json:2.0-RC3',
                'org.restlet.gae:org.restlet.lib.org.json:2.0',
                'com.sun.jersey:jersey-core:1.9.1',
                'com.sun.jersey:jersey-server:1.9.1',
                //'com.sun.jersey:jersey-json:1.9.1',
                'com.sun.jersey.contribs:jersey-spring:1.9.1',
                'javax.ws.rs:jsr311-api:1.1.1',
                // until RequestStreamAdapter is re-implemented ...
                'org.springframework:spring-test:3.1.0.RELEASE'
        ) {
           transitive = false
        }
    }

}

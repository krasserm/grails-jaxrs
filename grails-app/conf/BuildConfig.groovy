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

grails.project.work.dir = 'target'
grails.project.source.level = 1.6

/*
 * in order to publish snapshots into the dedicated repo you should add to ~/.grails/settings.groovy the following entries
 * 
 * grails.project.repos.jaxrssnapshotsrepo.username = "yourlogin"
 * grails.project.repos.jaxrssnapshotsrepo.password = "yourpassword"
 * 
 */
grails.project.repos.jaxrssnapshotsrepo.url = "http://noams.artifactoryonline.com/noams/grails-jaxrs-plugin-snapshots"
grails.project.repos.jaxrssnapshotsrepo.type = "maven"

grails.project.dependency.resolution = {
    inherits('global') {
        excludes 'hibernate'
    }
    log "warn"

    /*
     * legacyResolve is needed in order to use release plugin 2.2.0 with grails 2.2.1 
     * see http://www.objectpartners.com/2013/02/13/grails-2-2-publishing-your-plugins-as-maven-artifacts-to-resolve-dependency-resolution-issues/
     */
    legacyResolve true

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
        mavenRepo "http://maven.restlet.org"
    }

    dependencies {
        compile 'asm:asm:3.3'

        String restletVersion = '2.0.0'
        String jerseyVersion = '1.14'

        compile "org.restlet.gae:org.restlet:$restletVersion"

        compile("org.restlet.gae:org.restlet.ext.servlet:$restletVersion") {
            excludes 'org.restlet', 'servlet-api'
        }

        // A modified version (with removed META-INF/services/javax.ws.rs.ext.RuntimeDelegate)
        // is contained in the project's lib folder. This is needed because of a bug described
        // at http://restlet.tigris.org/issues/show_bug.cgi?id=1251
//        compile "org.restlet.gae:org.restlet.ext.jaxrs:$restletVersion"

        compile("org.restlet.gae:org.restlet.ext.json:$restletVersion") {
            excludes 'org.restlet', 'org.restlet.lib.org.json'
        }

        compile("com.sun.jersey:jersey-core:$jerseyVersion") {
            excludes 'jaxb-api', 'jsr311-api', 'junit', 'mail', 'org.osgi.core'
        }

        compile("com.sun.jersey:jersey-servlet:$jerseyVersion") {
            excludes 'ant', 'commons-io', 'javax.ejb', 'javax.servlet-api', 'jsp-api', 'junit', 'osgi_R4_core', 'persistence-api', 'weld-osgi-bundle'
        }

        compile("com.sun.jersey:jersey-server:$jerseyVersion") {
            excludes 'asm', 'commons-io', 'jaxb-api', 'jsr250-api', 'junit', 'mail', 'osgi_R4_core'
        }

        compile("com.sun.jersey:jersey-json:$jerseyVersion") {
            excludes 'jackson-core-asl', 'jackson-jaxrs', 'jackson-mapper-asl', 'jackson-xc', 'jaxb-impl', 'jettison', 'junit', 'org.eclipse.persistence.moxy'
        }

        compile("com.sun.jersey.contribs:jersey-spring:$jerseyVersion") {
            excludes 'jaxb-impl', 'jsr250-api', 'junit', 'servlet-api', 'testng'
        }

        compile('javax.ws.rs:jsr311-api:1.1.1') {
            excludes 'junit'
        }

        // until RequestStreamAdapter is re-implemented ...
        compile('org.springframework:spring-test:3.1.2.RELEASE') {
            excludes 'junit'
        }

        /*
         * needed for spock from grails 2.2
         * see http://code.google.com/p/grails-jaxrs/issues/detail?id=74 and http://grails.org/plugin/spock
         */
        compile "org.spockframework:spock-grails-support:0.7-groovy-2.0"
    }

    plugins {
        compile(':release:2.2.0', ':rest-client-builder:1.0.3') {
            export = false
        }

        compile(":spock:0.7") {
            exclude "spock-grails-support"
        }
    }
}

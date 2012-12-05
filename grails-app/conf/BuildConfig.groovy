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

grails.project.dependency.resolution = {

    inherits "global"
    log "warn"

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
            excludes 'servlet-api', 'org.restlet'
        }

        // A modified version (with removed META-INF/services/javax.ws.rs.ext.RuntimeDelegate)
        // is contained in the project's lib folder. This is needed because of a bug described
        // at http://restlet.tigris.org/issues/show_bug.cgi?id=1251
//        compile "org.restlet.gae:org.restlet.ext.jaxrs:$restletVersion"

        compile("org.restlet.gae:org.restlet.ext.json:$restletVersion") {
            excludes 'org.restlet.lib.org.json', 'org.restlet'
        }

        compile("com.sun.jersey:jersey-core:$jerseyVersion") {
            excludes 'jsr311-api', 'mail', 'jaxb-api', 'osgi_R4_core', 'junit'
        }

        compile("com.sun.jersey:jersey-servlet:$jerseyVersion") {
            excludes 'javax.servlet-api', 'jsp-api', 'javax.ejb', 'weld-osgi-bundle',
                     'persistence-api', 'ant', 'osgi_R4_core', 'junit', 'commons-io'
        }

        compile("com.sun.jersey:jersey-server:$jerseyVersion") {
            excludes 'asm', 'mail', 'jaxb-api', 'jsr250-api', 'osgi_R4_core', 'junit', 'commons-io'
        }

        compile("com.sun.jersey:jersey-json:$jerseyVersion") {
            excludes 'jettison', 'jaxb-impl', 'jackson-core-asl', 'jackson-mapper-asl',
                     'jackson-jaxrs', 'jackson-xc', 'junit'
        }

        compile("com.sun.jersey.contribs:jersey-spring:$jerseyVersion") {
            excludes 'servlet-api', 'testng', 'jaxb-impl', 'jsr250-api', 'junit'
        }

        compile('javax.ws.rs:jsr311-api:1.1.1') {
            excludes 'junit'
        }

        // until RequestStreamAdapter is re-implemented ...
        compile('org.springframework:spring-test:3.1.2.RELEASE') {
            excludes 'activation', 'el-api', 'javax.inject', 'persistence-api', 'portlet-api',
                     'servlet-api', 'jsp-api', 'geronimo-jta_1.1_spec', 'standard', 'aspectjweaver',
                     'hibernate-core', 'hibernate-cglib-repack', 'hsqldb', 'junit', 'spring-beans',
                     'spring-context', 'spring-core', 'spring-jdbc', 'spring-orm', 'spring-tx',
                     'spring-web', 'spring-webmvc-portlet', 'spring-webmvc', 'testng'
        }
    }

    plugins {
        build(':release:2.2.0', ':rest-client-builder:1.0.3') {
            export = false
        }
    }
}

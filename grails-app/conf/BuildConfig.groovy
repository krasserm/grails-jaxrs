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

grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
grails.project.work.dir = 'target/work'
grails.project.target.level = 1.7
grails.project.source.level = 1.7

grails.project.dependency.distribution = {
    remoteRepository(id: 'snapshots-repo', url: 'http://noams.artifactoryonline.com/noams/grails-jaxrs-plugin-snapshots') {
        authentication username: System.getProperty('DEPLOYER_USERNAME'), password: System.getProperty('DEPLOYER_PASSWORD')
    }
}
grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {

    inherits('global') {
        excludes 'hibernate'
    }
    log 'warn'

    repositories {
        mavenRepo 'http://jcenter.bintray.com'
        grailsCentral()
        mavenLocal()
        mavenRepo 'http://maven.restlet.org'
        mavenRepo 'http://noams.artifactoryonline.com/noams/grails-jaxrs-plugin-libs'
    }

    dependencies {
        String restletVersion = '2.1.4'
        String jerseyVersion = '1.17'

        compile "org.restlet.gae:org.restlet:$restletVersion"

        compile("org.restlet.gae:org.restlet.ext.servlet:$restletVersion") {
            excludes 'org.restlet', 'servlet-api'
        }

        // A modified version (with removed META-INF/services/javax.ws.rs.ext.RuntimeDelegate)
        // is contained in project's custom lib directory and repository. This is needed because of a bug described
        // at http://restlet.tigris.org/issues/show_bug.cgi?id=1251
        provided group: 'org.restlet.gae',
                name: 'org.restlet.ext.jaxrs-noruntimedel',
                version: restletVersion

        compile("org.restlet.gae:org.restlet.ext.json:$restletVersion") {
            excludes 'org.restlet'
        }

        compile("com.sun.jersey:jersey-core:$jerseyVersion") {
            excludes 'jaxb-api', 'jsr311-api', 'junit', 'mail', 'org.osgi.core'
        }

        compile("com.sun.jersey:jersey-servlet:$jerseyVersion") {
            excludes 'ant', 'commons-io', 'javax.ejb', 'javax.servlet-api', 'jsp-api', 'junit', 'osgi_R4_core',
                    'persistence-api', 'weld-osgi-bundle'
        }

        compile("com.sun.jersey:jersey-server:$jerseyVersion") {
            excludes 'asm', 'commons-io', 'jaxb-api', 'jsr250-api', 'junit', 'mail', 'osgi_R4_core'
        }

        compile("com.sun.jersey:jersey-json:$jerseyVersion") {
            excludes 'jackson-core-asl', 'jackson-jaxrs', 'jackson-mapper-asl', 'jackson-xc', 'jaxb-impl', 'jettison', 'junit', 'org.eclipse.persistence.moxy'
        }

        compile("com.sun.jersey.contribs:jersey-spring:$jerseyVersion") {
            excludes 'jaxb-impl', 'jsr250-api', 'junit', 'servlet-api', 'testng', 'spring-core', 'spring-beans',
                    'spring-context', 'spring-web', 'spring-aop'
        }

        compile('javax.ws.rs:jsr311-api:1.1.1') {
            excludes 'junit'
        }
    }

    plugins {
        build(':release:3.0.1', ':rest-client-builder:1.0.3') {
            export = false
        }
    }
}

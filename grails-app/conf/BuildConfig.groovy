/*
 *
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
                'com.sun.jersey:jersey-core:1.5',
                'com.sun.jersey:jersey-server:1.5',
                'com.sun.jersey.contribs:jersey-spring:1.5',
                'javax.ws.rs:jsr311-api:1.1',
		        'org.restlet.jse:org.restlet:2.0.5',
		        'org.restlet.jee:org.restlet.ext.jaxrs:2.0.5' /** Collision with jersey-core's jaxrs classes */,
                'org.restlet.jee:org.restlet.ext.servlet:2.0.5',
                //org.restlet.gae-2.0-RC3.jar /** Unable to identify the correct artifact */,
                'org.restlet.jee:org.restlet.ext.json:2.0.5'
        ){
           transitive=false
        }
        /*
        test( 'com.sun.jersey.jersey-test-framework:jersey-test-framework-grizzly:1.5'){
            //export=false
        }
        */
    }

}

#Grails JAX-RS Plugin

A [Grails](http://grails.org) plugin that supports the development of RESTful web services based on the [Java API for RESTful Web Services](http://jcp.org/en/jsr/detail?id=311) (JSR 311: JAX-RS).

It is targeted at developers who want to structure the web service layer of an application in a JSR 311 compatible way but still want to continue to use Grails' powerful features such as GORM, automated XML and JSON marshalling, Grails services, Grails filters and so on.
This plugin is an alternative to Grails' built-in mechanism for implementing RESTful web services. 

At the moment, plugin users may choose between [Jersey](https://jersey.dev.java.net) and [Restlet](http://www.restlet.org) as JAX-RS implementations. 
Both implementations are packaged with the plugin. 
Support for Restlet was added in version 0.2 of the plugin in order to support deployments to [Google App Engine](http://code.google.com/appengine).

Learn more at the [project wiki](https://github.com/krasserm/grails-jaxrs/wiki).

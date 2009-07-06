import org.grails.jaxrs.web.JaxrsFilterimport org.grails.jaxrs.web.JaxrsListenerclass JaxrsGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Martin Krasser"
    def authorEmail = "krasserm@googlemail.com"
    def title = "JSR 311 plugin for Grails"
    def description = """A plugin that supports the development of RESTful web services based on the Java API for RESTful Web Services (JSR 311: JAX-RS). It is targeted at developers who want to structure the web service layer of an application in a JSR 311 compatible way but still want to continue to use Grails' powerful features such as GORM, automated XML and JSON marshalling or Grails filters, to mention a few. This plugin is an alternative to Grails' built-in mechanism for implementing RESTful web services and is backed up by Jersey, the JSR 311 reference implementation. """

    // URL to the plugin's documentation
    def documentation = 'http://code.google.com/p/grails-jaxrs/'

    def doWithWebDescriptor = { xml ->        def lastListener = xml.'listener'.iterator().toList().last()
        lastListener + {
            'listener' {
                'listener-class'(JaxrsListener.class.name)
            }
        }
    
        def firstFilter = xml.'filter'[0]
        firstFilter + {
            'filter' {
                'filter-name'('jaxrsFilter')
                'filter-class'(JaxrsFilter.class.name)
            }
        }
    
        def firstFilterMapping = xml.'filter-mapping'[0]
        firstFilterMapping + {
            'filter-mapping' {
                'filter-name'('jaxrsFilter')
                'url-pattern'('/*')
                'dispatcher'('FORWARD')
                'dispatcher'('REQUEST')
            }
        }
        
    }

    def doWithSpring = {    }    def doWithApplicationContext = { applicationContext ->    }    def doWithDynamicMethods = { ctx ->
    }

    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }
}

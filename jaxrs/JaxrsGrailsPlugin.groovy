import static org.grails.jaxrs.web.JaxrsUtils.JAXRS_CONTEXT_NAME;import org.codehaus.groovy.grails.commons.ConfigurationHolderimport org.grails.jaxrs.DefaultGrailsResourceClassimport org.grails.jaxrs.ProviderArtefactHandlerimport org.grails.jaxrs.ResourceArtefactHandlerimport org.grails.jaxrs.provider.XMLWriterimport org.grails.jaxrs.web.JaxrsContextimport org.grails.jaxrs.web.JaxrsFilterimport org.grails.jaxrs.web.JaxrsListenerclass JaxrsGrailsPlugin {
    // the plugin version
    def version = "0.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]        def loadAfter = ['controllers','services']    def artefacts = [            new ResourceArtefactHandler(),            new ProviderArtefactHandler()    ]
    def watchedResources = [            "file:./grails-app/resources/**/*Resource.groovy",            "file:./plugins/*/grails-app/resources/**/*Resource.groovy",            "file:./grails-app/providers/**/*Reader.groovy",            "file:./grails-app/providers/**/*Writer.groovy",            "file:./plugins/*/grails-app/providers/**/*Reader.groovy",            "file:./plugins/*/grails-app/providers/**/*Writer.groovy"    ]
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

    def doWithSpring = {        // Configure the JAX-RS context         'jaxrsContext'(JaxrsContext)                // Configure default providers         "${XMLWriter.class.name}"(XMLWriter)                // Configure application-provided resources        application.resourceClasses.each { rc ->            "${rc.propertyName}"(rc.clazz) { bean ->                bean.scope = this.resourceScope                bean.autowire = true            }        }                // Configure application-provided providers        application.providerClasses.each { pc ->            "${pc.propertyName}"(pc.clazz) { bean ->                bean.scope = 'singleton'                bean.autowire = true            }        }    }    def onChange = { event ->            if (!event.ctx) {            return        }        if (application.isArtefactOfType(ResourceArtefactHandler.TYPE, event.source)) {            def resourceClass = application.addArtefact(ResourceArtefactHandler.TYPE, event.source)            beans {                "${resourceClass.propertyName}"(resourceClass.clazz) { bean ->                    bean.scope = this.resourceScope                    bean.autowire = true                }            }.registerBeans(event.ctx)        } else if (application.isArtefactOfType(ProviderArtefactHandler.TYPE, event.source)) {            def providerClass = application.addArtefact(ProviderArtefactHandler.TYPE, event.source)            beans {                "${providerClass.propertyName}"(providerClass.clazz) { bean ->                    bean.scope = 'singleton'                    bean.autowire = true                }            }.registerBeans(event.ctx)        } else {            return        }        event.ctx.getBean(JAXRS_CONTEXT_NAME).refresh()    }    def doWithApplicationContext = { applicationContext ->    }    def doWithDynamicMethods = { ctx ->
    }

    def onConfigChange = { event ->
    }        String getResourceScope() {        def scope = ConfigurationHolder.config.org.grails.jaxrs.resource.scope        if (!scope) {            scope = 'prototype'        }        scope    }    
}

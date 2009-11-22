import static org.grails.jaxrs.web.JaxrsUtils.JAXRS_CONTEXT_NAME;import org.codehaus.groovy.grails.commons.ConfigurationHolderimport org.grails.jaxrs.DefaultGrailsResourceClassimport org.grails.jaxrs.ProviderArtefactHandlerimport org.grails.jaxrs.ResourceArtefactHandlerimport org.grails.jaxrs.generator.ResourceGeneratorimport org.grails.jaxrs.provider.DomainObjectReaderimport org.grails.jaxrs.provider.DomainObjectWriter//import org.grails.jaxrs.provider.JSONReaderimport org.grails.jaxrs.provider.JSONWriterimport org.grails.jaxrs.provider.XMLReaderimport org.grails.jaxrs.provider.XMLWriterimport org.grails.jaxrs.web.JaxrsContextimport org.grails.jaxrs.web.JaxrsFilterimport org.grails.jaxrs.web.JaxrsListenerclass JaxrsGrailsPlugin {
    // the plugin version
    def version = "0.3"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/domain/*",            "grails-app/providers/*",            "grails-app/resources/*",            "grails-app/views/error.gsp",            "lib/*-sources.jar"
    ]        def loadAfter = ['controllers','services']    def artefacts = [            new ResourceArtefactHandler(),            new ProviderArtefactHandler()    ]
    def watchedResources = [            "file:./grails-app/resources/**/*Resource.groovy",            "file:./grails-app/providers/**/*Reader.groovy",            "file:./grails-app/providers/**/*Writer.groovy",            "file:./plugins/*/grails-app/resources/**/*Resource.groovy",            "file:./plugins/*/grails-app/providers/**/*Reader.groovy",            "file:./plugins/*/grails-app/providers/**/*Writer.groovy"    ]
    def author = "Martin Krasser"
    def authorEmail = "krasserm@googlemail.com"
    def title = "JSR 311 plugin"
    def description = """A plugin that supports the development of RESTful web services based on the Java API for RESTful Web Services (JSR 311: JAX-RS). It is targeted at developers who want to structure the web service layer of an application in a JSR 311 compatible way but still want to continue to use Grails' powerful features such as GORM, automated XML marshalling, Grails filters and so on. This plugin is an alternative to Grails' built-in mechanism for implementing RESTful web services.At the moment, plugin users may choose between Jersey (version 1.1.1-ea) and Restlet (version 2.0-m4) as JAX-RS implementation. Both implementations are packaged with the plugin. Support for Restlet was added in version 0.2 of the plugin in order to support deployments on the Google App Engine. Other JAX-RS implementations such as RestEasy or Apache Wink are likely to be added in upcoming versions of the plugin. """

    // URL to the plugin's documentation
    def documentation = 'http://code.google.com/p/grails-jaxrs/'
    /**     * Adds the JaxrsFilter and JaxrsListener to the web application      * descriptor.      */
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

    /**     * Adds the JaxrsContext and plugin- and application-specific JAX-RS      * resource and provider classes to the application context.     */    def doWithSpring = {        // Configure the JAX-RS context         'jaxrsContext'(JaxrsContext)                // Configure default providers        "${XMLWriter.class.name}"(XMLWriter)        "${XMLReader.class.name}"(XMLReader)        "${JSONWriter.class.name}"(JSONWriter)        //"${JSONReader.class.name}"(JSONReader)                // Experimental        //"${DomainObjectReader.class.name}"(DomainObjectReader)        //"${DomainObjectWriter.class.name}"(DomainObjectWriter)                // Configure application-provided resources        application.resourceClasses.each { rc ->            "${rc.propertyName}"(rc.clazz) { bean ->                bean.scope = this.resourceScope                bean.autowire = true            }        }                // Configure application-provided providers        application.providerClasses.each { pc ->            "${pc.propertyName}"(pc.clazz) { bean ->                bean.scope = 'singleton'                bean.autowire = true            }        }                // Configure the resource code generator        "${ResourceGenerator.class.name}"(ResourceGenerator)    }    /**     * Updates application-specific JAX-RS resource and provider classes in      * the application context.     */    def onChange = { event ->            if (!event.ctx) {            return        }        if (application.isArtefactOfType(ResourceArtefactHandler.TYPE, event.source)) {            def resourceClass = application.addArtefact(ResourceArtefactHandler.TYPE, event.source)            beans {                "${resourceClass.propertyName}"(resourceClass.clazz) { bean ->                    bean.scope = this.resourceScope                    bean.autowire = true                }            }.registerBeans(event.ctx)        } else if (application.isArtefactOfType(ProviderArtefactHandler.TYPE, event.source)) {            def providerClass = application.addArtefact(ProviderArtefactHandler.TYPE, event.source)            beans {                 "${providerClass.propertyName}"(providerClass.clazz) { bean ->                    bean.scope = 'singleton'                    bean.autowire = true                }            }.registerBeans(event.ctx)        } else {            return        }                // Setup the JaxrsConfig        doWithApplicationContext(event.ctx)                // Resfresh the JaxrsContext        event.ctx.getBean(JAXRS_CONTEXT_NAME).refresh()    }    /**     * Reconfigures the JaxrsConfig with plugin- and application-specific     * JAX-RS resource and provider classes. Configures the JaxrsContext     * with the JAX-RS implementation to use. The name of the JAX-RS      * implementation is obtained from the configuration property     * <code>org.grails.jaxrs.provider.name</code>. Default value is     * <code>jersey</code>.     */    def doWithApplicationContext = { applicationContext ->        def context = applicationContext.getBean(JAXRS_CONTEXT_NAME)        def config = context.jaxrsConfig                context.jaxrsProviderName = this.providerName                config.reset()        config.classes << XMLWriter.class        config.classes << XMLReader.class        config.classes << JSONWriter.class        //config.classes << JSONReader.class                // Experimental        //config.classes << DomainObjectReader.class        //config.classes << DomainObjectWriter.class                application.getArtefactInfo('Resource').classesByName.values().each { clazz ->            config.classes << clazz        }        application.getArtefactInfo('Provider').classesByName.values().each { clazz ->            config.classes << clazz        }            }    /**     *      */    def doWithDynamicMethods = { ctx ->    }

    /**     *      */    def onConfigChange = { event ->
    }        private String getResourceScope() {        def scope = ConfigurationHolder.config.org.grails.jaxrs.resource.scope        if (!scope) {            scope = 'prototype'        }        scope    }    private String getProviderName() {        def name = ConfigurationHolder.config.org.grails.jaxrs.provider.name        if (!name) {            name = JaxrsContext.JAXRS_PROVIDER_NAME_JERSEY        }        name    }    }

import org.codehaus.groovy.grails.web.servlet.GrailsDispatcherServlet
import org.grails.jaxrs.ProviderArtefactHandler
import org.grails.jaxrs.ResourceArtefactHandler
import org.grails.jaxrs.generator.CodeGenerator
import org.grails.jaxrs.provider.*
import org.grails.jaxrs.web.JaxrsContext
import org.grails.jaxrs.web.JaxrsFilter
import org.grails.jaxrs.web.JaxrsListener

import static org.grails.jaxrs.web.JaxrsUtils.JAXRS_CONTEXT_NAME

class JaxrsGrailsPlugin {
    def groupId = "org.grails.plugins"
    def version = "0.11"
    def grailsVersion = "2.4 > *"
    def pluginExcludes = [
            "grails-app/domain/*",
            "grails-app/providers/*",
            "grails-app/resources/*",
            "src/groovy/org/grails/jaxrs/test/*",
            "lib/*-sources.jar"
    ]

    def loadAfter = ['controllers', 'services', 'spring-security-core']
    def artefacts = [
            new ResourceArtefactHandler(),
            new ProviderArtefactHandler()
    ]
    def watchedResources = [
            "file:./grails-app/resources/**/*Resource.groovy",
            "file:./grails-app/providers/**/*Reader.groovy",
            "file:./grails-app/providers/**/*Writer.groovy",
            "file:./plugins/*/grails-app/resources/**/*Resource.groovy",
            "file:./plugins/*/grails-app/providers/**/*Reader.groovy",
            "file:./plugins/*/grails-app/providers/**/*Writer.groovy"
    ]

    def author = "Martin Krasser"
    def authorEmail = "krasserm@googlemail.com"
    def title = "JSR 311 plugin"
    def description = """
A plugin that supports the development of RESTful web services based on the
Java API for RESTful Web Services (JSR 311: JAX-RS). It is targeted at
developers who want to structure the web service layer of an application in
a JSR 311 compatible way but still want to continue to use Grails' powerful
features such as GORM, automated XML and JSON marshalling, Grails services,
Grails filters and so on. This plugin is an alternative to Grails' built-in
mechanism for implementing  RESTful web services.

At the moment, plugin users may choose between Jersey and Restlet as JAX-RS
implementation. Both implementations are packaged with the plugin. Support for
Restlet was added in version 0.2 of the plugin in order to support deployments
on the Google App Engine. Other JAX-RS implementations such as RestEasy or
Apache Wink are likely to be added in upcoming versions of the plugin.
"""

    def developers = [
            [name: 'Davide Cavestro', email: 'davide.cavestro@gmail.com'],
            [name: 'Noam Y. Tenne', email: 'noam@10ne.org']
    ]

    def documentation = 'https://github.com/krasserm/grails-jaxrs/wiki'

    def issueManagement = [url: 'https://github.com/krasserm/grails-jaxrs/issues']

    def scm = [url: 'https://github.com/krasserm/grails-jaxrs']

    /**
     * Adds the JaxrsFilter and JaxrsListener to the web application
     * descriptor.
     */
    def doWithWebDescriptor = { xml ->

        def lastListener = xml.'listener'.iterator().toList().last()
        lastListener + {
            'listener' {
                'listener-class'(JaxrsListener.name)
            }
        }

        def firstFilter = xml.'filter'[0]
        firstFilter + {
            'filter' {
                'filter-name'('jaxrsFilter')
                'filter-class'(JaxrsFilter.name)
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

        def grailsServlet = xml.servlet.find { servlet ->

            'grails'.equalsIgnoreCase(servlet.'servlet-name'.text())

        }

        // reload default GrailsDispatcherServlet adding 'dispatchOptionsRequest':'true'
        grailsServlet.replaceNode { node ->
            'servlet' {
                'servlet-name'('grails')
                'servlet-class'(GrailsDispatcherServlet.name)
                'init-param' {
                    'param-name'('dispatchOptionsRequest')
                    'param-value'('true')
                }
                'load-on-startup'('1')
            }
        }
    }

    /**
     * Adds the JaxrsContext and plugin- and application-specific JAX-RS
     * resource and provider classes to the application context.
     */
    def doWithSpring = {

        // Configure the JAX-RS context
        'jaxrsContext'(JaxrsContext)

        // Configure default providers
        "${XMLWriter.name}"(XMLWriter)
        "${XMLReader.name}"(XMLReader)
        "${JSONWriter.name}"(JSONWriter)
        "${JSONReader.name}"(JSONReader)
        "${DomainObjectReader.name}"(DomainObjectReader)
        "${DomainObjectWriter.name}"(DomainObjectWriter)

        // Configure application-provided resources
        application.resourceClasses.each { rc ->
            "${rc.propertyName}"(rc.clazz) { bean ->
                bean.scope = owner.getResourceScope(application)
                bean.autowire = true
            }
        }

        // Configure application-provided providers
        application.providerClasses.each { pc ->
            "${pc.propertyName}"(pc.clazz) { bean ->
                bean.scope = 'singleton'
                bean.autowire = true
            }
        }

        // Configure the resource code generator
        "${CodeGenerator.name}"(CodeGenerator)
    }

    /**
     * Updates application-specific JAX-RS resource and provider classes in
     * the application context.
     */
    def onChange = { event ->

        if (!event.ctx) {
            return
        }

        if (application.isArtefactOfType(ResourceArtefactHandler.TYPE, event.source)) {
            def resourceClass = application.addArtefact(ResourceArtefactHandler.TYPE, event.source)
            beans {
                "${resourceClass.propertyName}"(resourceClass.clazz) { bean ->
                    bean.scope = owner.getResourceScope(application)
                    bean.autowire = true
                }
            }.registerBeans(event.ctx)
        } else if (application.isArtefactOfType(ProviderArtefactHandler.TYPE, event.source)) {
            def providerClass = application.addArtefact(ProviderArtefactHandler.TYPE, event.source)
            beans {
                "${providerClass.propertyName}"(providerClass.clazz) { bean ->
                    bean.scope = 'singleton'
                    bean.autowire = true
                }
            }.registerBeans(event.ctx)
        } else {
            return
        }

        // Setup the JaxrsConfig
        doWithApplicationContext(event.ctx)

        // Resfresh the JaxrsContext
        event.ctx.getBean(JAXRS_CONTEXT_NAME).refresh()
    }

    /**
     * Reconfigures the JaxrsConfig with plugin- and application-specific
     * JAX-RS resource and provider classes. Configures the JaxrsContext
     * with the JAX-RS implementation to use. The name of the JAX-RS
     * implementation is obtained from the configuration property
     * <code>org.grails.jaxrs.provider.name</code>. Default value is
     * <code>jersey</code>.
     */
    def doWithApplicationContext = { applicationContext ->

        def context = applicationContext.getBean(JAXRS_CONTEXT_NAME)
        def config = context.jaxrsConfig

        context.jaxrsProviderName = getProviderName(application)
        context.jaxrsProviderExtraPaths = getProviderExtraPaths(application)
        context.jaxrsProviderInitParameters = getProviderInitParameters(application)

        config.reset()
        config.classes << XMLWriter
        config.classes << XMLReader
        config.classes << JSONWriter
        config.classes << JSONReader
        config.classes << DomainObjectReader
        config.classes << DomainObjectWriter

        application.getArtefactInfo('Resource').classesByName.values().each { clazz ->
            config.classes << clazz
        }
        application.getArtefactInfo('Provider').classesByName.values().each { clazz ->
            config.classes << clazz
        }
    }

    private String getResourceScope(application) {
        def scope = application.config.org.grails.jaxrs.resource.scope
        if (!scope) {
            scope = 'prototype'
        }
        scope
    }

    private String getProviderName(application) {
        def name = application.config.org.grails.jaxrs.provider.name
        if (!name) {
            name = JaxrsContext.JAXRS_PROVIDER_NAME_JERSEY
        }
        name
    }

    private String getProviderExtraPaths(application) {
        application.config.org.grails.jaxrs.provider.extra.paths
    }

    private Map<String, String> getProviderInitParameters(application) {
        application.config.org.grails.jaxrs.provider.init.parameters
    }
}

import grails.util.GrailsNameUtils// Assumptions://// - eventCreatedFile is called before eventCreatedArtefact// - instances of this script are never executed concurrently
def createdFile

eventCreatedFile = { file ->    createdFile = file}eventCreatedArtefact = { type, name ->   if (type == 'Resource') {       def resourceName = name       def resourcePath = GrailsNameUtils.getPropertyName(name)       ant.replace(file: createdFile, token: "@resource.name@", value: resourceName)       ant.replace(file: createdFile, token: "@resource.path@", value: resourcePath)   }
   }



<%=packageName ? "package ${packageName}\n\n" : ''%>import org.grails.jaxrs.provider.DomainObjectNotFoundException

class ${resourceName}ResourceService {

    def create(${resourceName} dto) {
        dto.save()
    }

    def read(id) {
        def obj = ${resourceName}.get(id)
        if (!obj) {
            throw new DomainObjectNotFoundException(${resourceName}.class, id)
        }
        obj
    }

    def readAll() {
        ${resourceName}.findAll()
    }

    def update(${resourceName} dto) {
        def obj = ${resourceName}.get(dto.id)
        if (!obj) {
            throw new DomainObjectNotFoundException(${resourceName}.class, dto.id)
        }
        obj.properties = dto.properties
        obj
    }

    void delete(id) {
        def obj = ${resourceName}.get(id)
        if (obj) {
            obj.delete()
        }
    }
}

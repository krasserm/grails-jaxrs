/*
 * Copyright 2009 the original author or authors.
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
package org.grails.jaxrs.provider

import org.grails.jaxrs.support.DomainObjectReaderSupport

import javax.ws.rs.Consumes
import javax.ws.rs.ext.Provider

/**
 * A concrete domain object reader that provides the same functionality as
 * {@link AbstractDomainObjectReader}. It can be disabled by setting
 * <code>org.grails.jaxrs.doreader.disable</code> to <code>true</code> in the
 * application config.
 *
 * @see AbstractDomainObjectReader
 *
 * @author Martin Krasser
 */
@Provider
@Consumes(['text/xml', 'application/xml', 'text/x-json', 'application/json'])
class DomainObjectReader extends DomainObjectReaderSupport {

    protected boolean isEnabled() {
        !grailsApplication.config.org.grails.jaxrs.doreader.disable
    }
}

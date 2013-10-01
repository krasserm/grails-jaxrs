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
package org.grails.jaxrs.support

import grails.plugin.spock.UnitSpec

import static org.grails.jaxrs.support.ConverterUtils.jsonToMap
import static org.grails.jaxrs.support.ConverterUtils.xmlToMap
/**
 * @author Martin Krasser
 * @author Noam Y. Tenne
 */
class ConverterUtilsSpec extends UnitSpec {

    private static final String encoding = 'UTF-8'

    def 'Transform XML to map'() {
        given:
        def xml = '<x id=\"1\"><y>2</y><z>3</z></x>'

        when:
        def map = mapFromXml(xml)

        then:
        map.id == '1'
        map.y == '2'
        map.z == '3'
    }

    def 'Transform XML with non-numeric ID to map'() {
        given:
        def xml = '<x id="a"><y>2</y><z>3</z></x>'

        when:
        def map = mapFromXml(xml)

        then:
        map.id == 'a'
    }

    def 'Transform simple JSON to a simple map'() {
        given:
        def json = '{"firstName" : "leeroy", "lastName" : "jenkins", "stats" : {"xp": 42} }'

        when:
        def map = simpleMapFromJson(json)

        then:
        map.firstName == 'leeroy'
        map.lastName == 'jenkins'
        map.stats.xp == 42
    }

    def 'Transform domain JSON to a simple map'() {
        given:
        def json = '{"class":"Player", "firstName" : "leeroy", "lastName" : "jenkins"}'

        when:
        def map = simpleMapFromJson(json)

        then:
        map.firstName == 'leeroy'
        map.lastName == 'jenkins'
        map.class == 'Player'
    }

    private Map mapFromXml(def xml) {
        xmlToMap(new ByteArrayInputStream(xml.getBytes(encoding)), encoding)
    }

    private Map simpleMapFromJson(def json) {
        jsonToMap(new ByteArrayInputStream(json.getBytes(encoding)), encoding)
    }
}

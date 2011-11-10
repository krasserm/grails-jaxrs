package org.grails.jaxrs.test

import javax.ws.rs.Path
import javax.ws.rs.GET
import javax.ws.rs.ext.MessageBodyReader
import javax.ws.rs.ext.Provider
import org.junit.Ignore

@Path('/a') class TestA { @GET String a() {'a'} }
@Path('/b') class TestB { String b() {'b'} }
class TestC { @GET String c() {'c'} }
class TestD { String d() {'d'} }
abstract class TestE implements MessageBodyReader { @GET String e() {'e'} }

@Path('/a') class TestH1A {}
class TestH1B extends TestH1A {}

class TestH2A {}
class TestH2B extends TestH2A {}

@Path('/a') @Ignore interface TestH3A {}
class TestH3B implements TestH3A {}


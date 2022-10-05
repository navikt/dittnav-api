package no.nav.personbruker.dittnav.api.personalia;

import io.ktor.client.request.get
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test

class PersonaliaApiTest {

    @Test
    fun testGetIdent() = testApplication {
        client.get("/ident").apply {
        //    TODO("Please write your test here")
        }
    }

    @Test
    fun testGetNavn() = testApplication {

        client.get("/navn").apply {
         //   TODO("Please write your test here")
        }
    }
}
package no.nav.personbruker.dittnav.api.saker;

import io.ktor.client.request.get
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test


class SakerApiTest {

    @Test
    fun testGetSaker() = testApplication {
        client.get("/saker").apply {
         //   TODO("Please write your test here")
        }
    }
}
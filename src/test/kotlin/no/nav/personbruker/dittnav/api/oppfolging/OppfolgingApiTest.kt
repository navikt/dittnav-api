package no.nav.personbruker.dittnav.api.oppfolging;

import io.ktor.client.request.get
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test


class OppfolgingApiTest {

    @Test
    fun testGetOppfolging() = testApplication {
        client.get("/oppfolging").apply {
        //    TODO("Please write your test here")
        }
    }
}

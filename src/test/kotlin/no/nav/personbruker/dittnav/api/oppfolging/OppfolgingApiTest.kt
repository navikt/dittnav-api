package no.nav.personbruker.dittnav.api.oppfolging;

import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class OppfolgingApiTest {

    @Test
    fun testGetOppfolging() = testApplication {
        client.get("/oppfolging").apply {
        //    TODO("Please write your test here")
        }
    }
}
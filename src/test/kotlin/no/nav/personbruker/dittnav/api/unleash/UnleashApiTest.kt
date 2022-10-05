package no.nav.personbruker.dittnav.api.unleash;

import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class UnleashApiTest {

    @Test
    fun testGetUnleashMinside() = testApplication {

        client.get("/unleash/minside").apply {
       //     TODO("Please write your test here")
        }
    }

    @Test
    fun testGetUnleashSituasjon() = testApplication {

        client.get("/unleash/situasjon").apply {
         //    TODO("Please write your test here")
        }
    }
}
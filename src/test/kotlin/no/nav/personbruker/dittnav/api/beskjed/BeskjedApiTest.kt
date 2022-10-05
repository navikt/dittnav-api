package no.nav.personbruker.dittnav.api.beskjed;

import io.ktor.client.request.get
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test


class BeskjedApiTest {

    @Test
    fun testGetBeskjed() = testApplication {
        val beskjedOject = createBeskjed(eventId = "12345", fodselsnummer = "9876543210", aktiv = true)
        val beskjedOject2 = createBeskjed(eventId = "12346", fodselsnummer = "9876543210", aktiv = true)
        val beskjedOject3 = createBeskjed(eventId = "12347", fodselsnummer = "5432109867", aktiv = true)


        client.get("/beskjed").apply {
            //TODO("Please write your test here")
        }
    }

    @Test
    fun testGetBeskjedInaktiv() = testApplication {

        client.get("/beskjed/inaktiv").apply {
            //TODO("Please write your test here")
        }
    }
}
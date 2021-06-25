package no.nav.personbruker.dittnav.api.beskjed

import io.ktor.http.*
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class BeskjedResultTest {

    @Test
    fun `Skal returnere http-kode ok hvis alle kilder svarer`() {
        val result = BeskjedResultObjectMother.createBeskjedResultWithoutErrors(1)

        result.determineHttpCode() `should be equal to` HttpStatusCode.OK
    }

    @Test
    fun `Skal returnere http-kode partial result hvis en kilde ikke svarer`() {
        val result = BeskjedResultObjectMother.createBeskjedResultWithOneError(1)

        result.determineHttpCode() `should be equal to` HttpStatusCode.PartialContent
    }

    @Test
    fun `Skal returnere http-kode service unavailable hvis ingen kilder svarer`() {
        val result = BeskjedResultObjectMother.createBeskjedResultWithTwoErrors()

        result.determineHttpCode() `should be equal to` HttpStatusCode.ServiceUnavailable
    }

}

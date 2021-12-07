package no.nav.personbruker.dittnav.api.saker

import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.config.json
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class SakerDTOTest {

    private val objectMapper = json()

    @Test
    fun `Skal kunne serialiseres til JSON`() {
        val dto = SakerDtoObjectMother.giveMeSisteSakstemaForDev()
        val json = objectMapper.encodeToString(dto)

        json.shouldNotBeNull()
    }

}

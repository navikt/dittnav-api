package no.nav.personbruker.dittnav.api.varsel

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.personbruker.dittnav.api.config.enableDittNavJsonConfig
import org.amshove.kluent.`should contain`
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

class VarselTest {

    private val eksempelVarselFraLegacyApi =
        """{"id":1,
            |"aktoerID":"123",
            |"url":"https://dummy.url",
            |"varseltekst":"Tekst",
            |"varselId":"4",
            |"meldingsType":"a",
            |"datoOpprettet":"2019-12-06T07:07:55.246+01:00[Europe/Oslo]",
            |"datoLest":"2020-02-04T08:02:13+01:00[Europe/Oslo]"}
            |""".trimMargin()

    val objectMapper = ObjectMapper().apply {
        enableDittNavJsonConfig()
    }

    @Test
    fun `skal returnere maskerte data fra toString-metoden`() {
        val varsel = createLestVarsel("1")
        val varselAsString = varsel.toString()
        varselAsString `should contain` "aktoerID=***"
        varselAsString `should contain` "tekst=***"
        varselAsString `should contain` "url=***"
    }

    @Test
    fun `skal kunne serialisere og deserialisere`() {
        val varsel = createLestVarsel("1000")
        val serialized = objectMapper.writeValueAsString(varsel)

        val deserialiser = objectMapper.readValue<Varsel>(serialized)

        deserialiser.shouldNotBeNull()
    }

    @Test
    fun `skal kunne deserialisere et eksempel-varsel fra legacy-api`() {
        val deserialized = objectMapper.readValue<Varsel>(eksempelVarselFraLegacyApi)

        deserialized.shouldNotBeNull()
    }

}

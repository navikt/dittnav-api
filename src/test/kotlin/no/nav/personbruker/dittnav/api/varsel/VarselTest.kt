@file:UseSerializers(ZonedDateTimeSerializer::class)
package no.nav.personbruker.dittnav.api.varsel

import kotlinx.serialization.UseSerializers
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import no.nav.personbruker.dittnav.api.common.serializer.ZonedDateTimeSerializer
import no.nav.personbruker.dittnav.api.config.json
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
        val serialized = json().encodeToString(varsel)

        val deserialiser = json().decodeFromString<Varsel>(serialized)

        deserialiser.shouldNotBeNull()
    }

    @Test
    fun `skal kunne deserialisere et eksempel-varsel fra legacy-api`() {
        val deserialized = json().decodeFromString<Varsel>(eksempelVarselFraLegacyApi)

        deserialized.shouldNotBeNull()
    }
}

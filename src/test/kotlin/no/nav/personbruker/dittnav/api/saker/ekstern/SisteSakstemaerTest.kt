package no.nav.personbruker.dittnav.api.saker.ekstern

import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.config.json
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class SisteSakstemaerTest {

    val mineSakerRespons = """
        {
          "sistEndrede" : [ {
            "navn" : "Økonomisk sosialhjelp",
            "kode" : "KOM",
            "sistEndret" : "2021-10-07T09:01:16.822189+02:00",
            "detaljvisningUrl" : "https://dummy/KOM"
          }, {
            "navn" : "Pensjon",
            "kode" : "PEN",
            "sistEndret" : "2021-08-13T09:01:16.826011+02:00",
            "detaljvisningUrl" : "https://dummy/PEN"
          } ],
          "dagpengerSistEndret" : "2021-07-04T09:01:16.826137+02:00"
        }
    """.trimIndent()

    val mineSakerResponsUtenDagpengerSistEndret = """
        {
          "sistEndrede" : [ {
            "navn" : "Økonomisk sosialhjelp",
            "kode" : "KOM",
            "sistEndret" : "2021-10-07T09:01:16.822189+02:00",
            "detaljvisningUrl" : "https://dummy/KOM"
          }, {
            "navn" : "Pensjon",
            "kode" : "PEN",
            "sistEndret" : "2021-08-13T09:01:16.826011+02:00",
            "detaljvisningUrl" : "https://dummy/PEN"
          } ],
          "dagpengerSistEndret" : null
        }
    """.trimIndent()

    @Test
    fun `Skal kunne deserialisere ekstern respons`() {
        val om = json()

        val deserialized = om.decodeFromString<SisteSakstemaer>(mineSakerRespons)

        deserialized.shouldNotBeNull()
    }

    @Test
    fun `Skal kunne deserialisere ekstern respons med nullable zoneddatetime`() {
        val om = json()

        val deserialized = om.decodeFromString<SisteSakstemaer>(mineSakerResponsUtenDagpengerSistEndret)

        deserialized.shouldNotBeNull()
    }

}

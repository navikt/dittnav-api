package no.nav.personbruker.dittnav.api.saker

import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.serialization.decodeFromString
import no.nav.personbruker.dittnav.api.config.jsonConfig
import org.junit.jupiter.api.Test

internal class SisteSakstemaerTest {

    private val objectMapper = jsonConfig()

    private val mineSakerRespons = """
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

    private val mineSakerResponsUtenDagpengerSistEndret = """
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
        val deserialized = objectMapper.decodeFromString<SisteSakstemaer>(mineSakerRespons)
        deserialized.shouldNotBeNull()
    }

    @Test
    fun `Skal kunne deserialisere ekstern respons med nullable zoneddatetime`() {
        val deserialized = objectMapper.decodeFromString<SisteSakstemaer>(mineSakerResponsUtenDagpengerSistEndret)
        deserialized.shouldNotBeNull()
    }

}

package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.toSpesificJsonFormat
import java.net.URL
import java.time.ZonedDateTime


//language=json
internal fun SakerDTO.mapToEksternJson(): String = """
    {
      "dagpengerSistEndret": "${this.dagpengerSistEndret}",
      "sistEndrede": ${this.sakstemaer.toSpesificJsonFormat(SakstemaDTO::mapToEksternJson)}
    }
""".trimIndent()

//language=json
internal fun SakstemaDTO.mapToEksternJson(): String = """
    {
        "navn":"$navn",
        "kode":"$kode",
        "sistEndret": "$sistEndret",
        "detaljvisningUrl": "$detaljvisningUrl"
    }
    
""".trimIndent()

internal fun createSakerDto(
    dagpengerSistEndret: ZonedDateTime? = ZonedDateTime.now(),
    sakstemaer: List<SakstemaDTO> = listOf(createSakstemaDTO()),
    minesakerTestUrl: String = "https://ut.nav/saker-test",
) = SakerDTO(sakstemaer = sakstemaer, sakerURL = URL(minesakerTestUrl), dagpengerSistEndret = dagpengerSistEndret)

internal fun createSakstemaDTO(
    navn: String = "navnernavn",
    kode: String = "kodeErKode",
    sistEndret: ZonedDateTime = ZonedDateTime.now(),
    detaljvisningUrl: String = "https://default.test"
) =
    SakstemaDTO(navn = navn, kode = kode, sistEndret = sistEndret, detaljvisningUrl = URL(detaljvisningUrl))

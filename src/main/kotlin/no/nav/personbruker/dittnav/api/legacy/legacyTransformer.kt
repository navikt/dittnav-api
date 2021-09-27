package no.nav.personbruker.dittnav.api.legacy

import no.nav.personbruker.dittnav.api.saker.SakerDTO
import java.net.URL

fun LegacySakstemaerRespons.toInternal() : List<SakerDTO> {
    return sakstemaList.map { external ->
        external.toInternal()
    }.toList()
}

fun LegacySakstema.toInternal(): SakerDTO {
    return SakerDTO(
        navn = temanavn,
        kode = temakode,
        sistEndret = sisteOppdatering,
        detaljvisningUrl = URL("https://TODO")
    )
}

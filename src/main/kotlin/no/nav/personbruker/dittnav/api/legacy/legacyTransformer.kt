package no.nav.personbruker.dittnav.api.legacy

import no.nav.personbruker.dittnav.api.saker.SakstemaDTO
import java.net.URL

fun LegacySakstemaerRespons.toInternal() : List<SakstemaDTO> {
    return sakstemaList.map { external ->
        external.toInternal()
    }.toList()
}

fun LegacySakstema.toInternal(): SakstemaDTO {
    return SakstemaDTO(
        navn = temanavn,
        kode = temakode,
        sistEndret = sisteOppdatering,
        detaljvisningUrl = URL("https://TODO")
    )
}

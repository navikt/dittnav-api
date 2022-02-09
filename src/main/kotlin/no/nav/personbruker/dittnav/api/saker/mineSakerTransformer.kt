package no.nav.personbruker.dittnav.api.saker

import no.nav.personbruker.dittnav.api.saker.ekstern.SisteSakstemaer

fun Sakstema.toInternal(): SakstemaDTO {
    return SakstemaDTO(
        navn = navn,
        kode = kode,
        sistEndret = sistEndret,
        detaljvisningUrl = detaljvisningUrl
    )
}

private fun List<Sakstema>.toInternal() : List<SakstemaDTO> {
    return filterNotNull().map { external ->
        external.toInternal()
    }.toList()
}

fun SisteSakstemaer.toInternal(): SisteSakstemaerDTO {
    return SisteSakstemaerDTO(
        sistEndrede.toInternal(),
        dagpengerSistEndret
    )
}

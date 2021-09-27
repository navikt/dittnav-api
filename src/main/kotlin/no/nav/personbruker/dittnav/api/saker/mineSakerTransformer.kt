package no.nav.personbruker.dittnav.api.saker

fun Sakstema.toInternal(): SakstemaDTO {
    return SakstemaDTO(
        navn = navn,
        kode = kode,
        sistEndret = sistEndret,
        detaljvisningUrl = detaljvisningUrl
    )
}

package no.nav.personbruker.dittnav.api.saker

fun Sakstema.toInternal(): SakerDTO {
    return SakerDTO(
        navn = navn,
        kode = kode,
        sistEndret = sistEndret,
        detaljvisningUrl = detaljvisningUrl
    )
}

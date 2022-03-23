package no.nav.personbruker.dittnav.api.mininnboks

enum class UbehandledeMeldingerType(val externalPath: String) {
    ULEST("ulest"), UBESVART("ubesvart"), OPPGAVE_VARSEL("oppgave"), DOKUMENT_VARSEL("dokument")
}

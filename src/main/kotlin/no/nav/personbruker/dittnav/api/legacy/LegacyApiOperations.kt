package no.nav.personbruker.dittnav.api.legacy

enum class LegacyApiOperations(
    val path : String
) {

    UBEHANDLEDE_MELDINGER( "/meldinger/ubehandlede"),
    PAABEGYNTESAKER( "/saker/paabegynte"),
    SAKSTEMA( "/saker/sakstema"),
    MELDEKORT( "/meldekortinfo"),
    OPPFOLGING( "/oppfolging");

}

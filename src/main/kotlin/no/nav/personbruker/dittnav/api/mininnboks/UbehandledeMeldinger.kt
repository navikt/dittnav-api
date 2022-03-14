package no.nav.personbruker.dittnav.api.mininnboks

data class UbehandledeMeldinger(
    val type: UbehandledeMeldingerType,
    val url: String,
    val antall: Int
)

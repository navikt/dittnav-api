package no.nav.personbruker.dittnav.api.personalia

import kotlinx.serialization.Serializable

@Serializable
data class Navn(
    val fornavn: String?,
    val mellomnavn: String?,
    val etternavn: String?
)

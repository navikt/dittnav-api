package no.nav.personbruker.dittnav.api.personalia

import kotlinx.serialization.Serializable

@Serializable
data class NavnExternal(
    val fornavn: String?,
    val mellomnavn: String?,
    val etternavn: String?
)

fun NavnExternal.toInternalNavnDTO(): NavnDTO {
    return NavnDTO(
        navn = concatenateToNavn(fornavn, mellomnavn, etternavn)
    )
}

private fun concatenateToNavn(fornavn: String?, mellomnavn: String?, etternavn: String?): String {
    return listOf(fornavn, mellomnavn, etternavn)
        .filter { navn -> !navn.isNullOrBlank() }
        .joinToString(" ")
}

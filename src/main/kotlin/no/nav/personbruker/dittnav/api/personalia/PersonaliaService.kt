package no.nav.personbruker.dittnav.api.personalia

import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser


class PersonaliaService (
    val personaliaConsumer: PersonaliaConsumer,
    val personaliaTokendings: PersonaliaTokendings
){

    suspend fun hentNavn(user: AuthenticatedUser): NavnDTO {
        val exchangedToken = personaliaTokendings.exchangeToken(user)
        val response = personaliaConsumer.hentNavn(user.ident, exchangedToken.value)
        val external = toExternalNavn(response).first()

        return external.toInternalNavnDTO()
    }

    fun hentIdent(user: AuthenticatedUser): IdentDTO {
        return IdentDTO(user.ident)
    }
}

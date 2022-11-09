package no.nav.personbruker.dittnav.api.personalia

import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser


class PersonaliaService (
    val navnConsumer: NavnConsumer,
    val navnTokendings: NavnTokendings
){

    suspend fun hentNavn(user: AuthenticatedUser): NavnDTO {
        val exchangedToken = navnTokendings.exchangeToken(user)
        val response = navnConsumer.hentNavn(user.ident, exchangedToken.value)
        val externalNavn = toExternalNavn(response).first()

        return externalNavn.toInternalNavnDTO()
    }

}

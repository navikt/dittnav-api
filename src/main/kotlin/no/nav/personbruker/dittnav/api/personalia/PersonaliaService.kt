package no.nav.personbruker.dittnav.api.personalia

import mu.KotlinLogging
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser


class PersonaliaService (
    val personaliaConsumer: PersonaliaConsumer,
    val personaliaTokendings: PersonaliaTokendings
){

    val log = KotlinLogging.logger { }

    suspend fun hentNavn(user: AuthenticatedUser): NavnDTO {
        log.info { "Bytter token for å hente navn" }
        val exchangedToken = personaliaTokendings.exchangeToken(user)
        val response = personaliaConsumer.hentNavn(user.ident, exchangedToken.value)
        log.info { "Response: $response" }
        val external = toExternalNavn(response).first()
        log.info { "Klarte å hente navn fra pdl" }

        return external.toInternalNavnDTO()
    }

    fun hentIdent(user: AuthenticatedUser): IdentDTO {
        return IdentDTO(user.ident)
    }
}

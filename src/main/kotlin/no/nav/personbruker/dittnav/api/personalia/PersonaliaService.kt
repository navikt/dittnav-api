package no.nav.personbruker.dittnav.api.personalia

import no.nav.personbruker.dittnav.api.common.ConsumePersonaliaException
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class PersonaliaService (
    val personaliaConsumer: PersonaliaConsumer,
    val personaliaTokendings: PersonaliaTokendings
){

    suspend fun hentNavn(user: AuthenticatedUser): PersonaliaNavnDTO {
        try {
            val exchangedToken = personaliaTokendings.exchangeToken(user)

            return personaliaConsumer.hentNavn(exchangedToken)
        } catch (e: Exception) {
            throw ConsumePersonaliaException("Klarte ikke å hente navn", e)
        }
    }

    suspend fun hentIdent(user: AuthenticatedUser): PersonaliaIdentDTO {
        try {
            val exchangedToken = personaliaTokendings.exchangeToken(user)

            return personaliaConsumer.hentIdent(exchangedToken)
        } catch (e: Exception) {
            throw ConsumePersonaliaException("Klarte ikke å hente ident", e)
        }
    }
}

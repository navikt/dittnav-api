package no.nav.personbruker.dittnav.api.oppgave

import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class OppgaveService(
    private val oppgaveConsumer: OppgaveConsumer,
    private val eventhandlerTokendings: EventhandlerTokendings
) {
    suspend fun getActiveOppgaver(user: AuthenticatedUser): List<OppgaveDTO> {
        val exchangedToken = eventhandlerTokendings.exchangeToken(user)
        return getOppgaveEvents(user, exchangedToken) {
            oppgaveConsumer.getExternalActiveEvents(it)
        }
    }

    suspend fun getInactiveOppgaver(user: AuthenticatedUser): List<OppgaveDTO> {
        val exchangedToken = eventhandlerTokendings.exchangeToken(user)
        return getOppgaveEvents(user, exchangedToken) {
            oppgaveConsumer.getExternalInactiveEvents(it)
        }
    }

    private suspend fun getOppgaveEvents(
        user: AuthenticatedUser,
        exchangedToken: AccessToken,
        oppgaveFetcher: suspend (AccessToken) -> List<Oppgave>
    ): List<OppgaveDTO> {
        return try {
            oppgaveFetcher(exchangedToken)
                .map { oppgave -> transformToDTO(oppgave, user.loginLevel) }
        } catch (e: Exception) {
            throw ConsumeEventException("Klarte ikke hente oppgaver", e)
        }
    }

    private fun transformToDTO(oppgave: Oppgave, operatingLoginLevel: Int): OppgaveDTO {
        return if(userIsAllowedToViewAllDataInEvent(oppgave, operatingLoginLevel)) {
            toOppgaveDTO(oppgave)
        } else {
            toMaskedOppgaveDTO(oppgave)
        }
    }

    private fun userIsAllowedToViewAllDataInEvent(beskjed: Oppgave, operatingLoginLevel: Int): Boolean {
        return operatingLoginLevel >= beskjed.sikkerhetsnivaa
    }
}

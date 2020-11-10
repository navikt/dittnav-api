package no.nav.personbruker.dittnav.api.oppgave

import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.api.loginstatus.LoginLevelService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class OppgaveService(private val oppgaveConsumer: OppgaveConsumer, private val loginLevelService: LoginLevelService) {

    suspend fun getActiveOppgaveEvents(user: AuthenticatedUser): List<OppgaveDTO> {
        return getOppgaveEvents(user) {
            oppgaveConsumer.getExternalActiveEvents(it)
        }
    }

    suspend fun getInactiveOppgaveEvents(user: AuthenticatedUser): List<OppgaveDTO> {
        return getOppgaveEvents(user) {
            oppgaveConsumer.getExternalInactiveEvents(it)
        }
    }

    private suspend fun getOppgaveEvents(
            user: AuthenticatedUser,
            getEvents: suspend (AuthenticatedUser) -> List<Oppgave>
    ): List<OppgaveDTO> {
        return try {
            val externalEvents = getEvents(user)
            val highestRequiredLoginLevel = getHighestRequiredLoginLevel(externalEvents)
            val operatingLoginLevel = loginLevelService.getOperatingLoginLevel(user, highestRequiredLoginLevel)
            externalEvents.map { oppgave -> transformToDTO(oppgave, operatingLoginLevel) }
        } catch (exception: Exception) {
            throw ConsumeEventException("Klarte ikke hente eventer av type Oppgave", exception)
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

    private fun getHighestRequiredLoginLevel(innboksList: List<Oppgave>): Int {
        return innboksList.maxOf { it.sikkerhetsnivaa }
    }
}

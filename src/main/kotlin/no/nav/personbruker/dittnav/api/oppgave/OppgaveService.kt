package no.nav.personbruker.dittnav.api.oppgave

import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.loginstatus.LoginLevelService
import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory

class OppgaveService(
    private val oppgaveConsumer: OppgaveConsumer,
    private val eventhandlerTokendings: EventhandlerTokendings,
    private val loginLevelService: LoginLevelService) {

    private val log = LoggerFactory.getLogger(OppgaveService::class.java)

    private val kilde = KildeType.EVENTHANDLER

    suspend fun getActiveOppgaveEvents(user: AuthenticatedUser): MultiSourceResult<OppgaveDTO, KildeType> {
        val exchangedToken = eventhandlerTokendings.exchangeToken(user)
        return getOppgaveEvents(user, exchangedToken) {
            oppgaveConsumer.getExternalActiveEvents(it)
        }
    }

    suspend fun getInactiveOppgaveEvents(user: AuthenticatedUser): MultiSourceResult<OppgaveDTO, KildeType> {
        val exchangedToken = eventhandlerTokendings.exchangeToken(user)
        return getOppgaveEvents(user, exchangedToken) {
            oppgaveConsumer.getExternalInactiveEvents(it)
        }
    }

    private suspend fun getOppgaveEvents(
        user: AuthenticatedUser,
        exchangedToken: AccessToken,
        getEvents: suspend (AccessToken) -> List<Oppgave>
    ): MultiSourceResult<OppgaveDTO, KildeType> {
        return try {
            val externalEvents = getEvents(exchangedToken)
            val highestRequiredLoginLevel = getHighestRequiredLoginLevel(externalEvents)
            val operatingLoginLevel = loginLevelService.getOperatingLoginLevel(user, highestRequiredLoginLevel)
            val oppgaver = externalEvents.map { oppgave -> transformToDTO(oppgave, operatingLoginLevel) }
            MultiSourceResult.createSuccessfulResult(oppgaver, kilde)

        } catch (e: Exception) {
            log.warn("Klarte ikke å hente data fra $kilde: $e", e)
            MultiSourceResult.createErrorResult(kilde)
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

    private fun getHighestRequiredLoginLevel(oppgaveList: List<Oppgave>): Int {
        return if (oppgaveList.isEmpty()) {
            0
        } else {
            oppgaveList.maxOf { it.sikkerhetsnivaa }
        }
    }
}

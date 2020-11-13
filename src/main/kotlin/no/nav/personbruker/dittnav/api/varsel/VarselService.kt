package no.nav.personbruker.dittnav.api.varsel

import no.nav.personbruker.dittnav.api.beskjed.BeskjedResult
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class VarselService(private val varselConsumer: VarselConsumer) {

    suspend fun getActiveVarselEvents(user: AuthenticatedUser): BeskjedResult {
        return getVarselEvents(user) {
            varselConsumer.getSisteVarsler(it).filter { varsel -> varsel.datoLest == null }
        }
    }

    suspend fun getInactiveVarselEvents(user: AuthenticatedUser): BeskjedResult {
        return getVarselEvents(user) {
            varselConsumer.getSisteVarsler(it).filter { varsel -> varsel.datoLest != null }
        }
    }

    private suspend fun getVarselEvents(
        user: AuthenticatedUser,
        getEvents: suspend (AuthenticatedUser) -> List<Varsel>
    ): BeskjedResult {
        return try {
            val externalEvents = getEvents(user)
            val results = externalEvents.map { varsel -> toVarselDTO(varsel) }
            BeskjedResult(results)
        } catch (exception: Exception) {
            BeskjedResult(listOf(KildeType.VARSELINNBOKS))
        }
    }
}

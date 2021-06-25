package no.nav.personbruker.dittnav.api.varsel

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class VarselService(private val varselConsumer: VarselConsumer) {

    private val kildetype = KildeType.VARSELINNBOKS

    suspend fun getActiveVarselEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        return getVarselEvents(user) {
            varselConsumer.getSisteVarsler(it).filter { varsel -> varsel.datoLest == null }
        }
    }

    suspend fun getInactiveVarselEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        return getVarselEvents(user) {
            varselConsumer.getSisteVarsler(it).filter { varsel -> varsel.datoLest != null }
        }
    }

    private suspend fun getVarselEvents(
        user: AuthenticatedUser,
        getEvents: suspend (AuthenticatedUser) -> List<Varsel>
    ): MultiSourceResult<BeskjedDTO, KildeType> {
        return try {
            val externalEvents = getEvents(user)
            val results = externalEvents.map { varsel -> toVarselDTO(varsel) }
            MultiSourceResult.createSuccessfulResult(results, kildetype)
        } catch (exception: Exception) {
            MultiSourceResult.createErrorResult(kildetype)
        }
    }
}

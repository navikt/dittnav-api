package no.nav.personbruker.dittnav.api.varsel

import no.nav.personbruker.dittnav.api.beskjed.BeskjedDTO
import no.nav.personbruker.dittnav.api.beskjed.KildeType
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory

class VarselService(private val varselConsumer: VarselConsumer) {

    private val log = LoggerFactory.getLogger(VarselService::class.java)

    private val kildetype = KildeType.VARSELINNBOKS

    suspend fun getActiveVarselEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        return getVarselEvents(user) {
            varselConsumer.getSisteVarsler(user).filter { varsel -> varsel.datoLest == null }
        }
    }

    suspend fun getInactiveVarselEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        return getVarselEvents(user) {
            varselConsumer.getSisteVarsler(user).filter { varsel -> varsel.datoLest != null }
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

        } catch (e: Exception) {
            log.warn("Klarte ikke å hente data fra $kildetype: $e", e)
            MultiSourceResult.createErrorResult(kildetype)
        }
    }
}

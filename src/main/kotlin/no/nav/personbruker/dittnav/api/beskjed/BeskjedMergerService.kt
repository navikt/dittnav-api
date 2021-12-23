package no.nav.personbruker.dittnav.api.beskjed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.api.varsel.VarselService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory

class BeskjedMergerService(
    private val beskjedService: BeskjedService,
    private val varselService: VarselService,
    private val digiSosService: DigiSosService,
    private val unleashService: UnleashService
) {

    private val log = LoggerFactory.getLogger(BeskjedMergerService::class.java)

    suspend fun getActiveEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> = withContext(Dispatchers.IO) {
        val beskjeder = async {
            beskjedService.getActiveBeskjedEvents(user)
        }

        val paabegynte = async {
            fetchActiveFromDigiSosIfEnabled(user)
        }

        val varslerSomBeskjed = async {
            fetchActiveFromVarselinnboksIfEnabled(user)
        }

        beskjeder.await() + paabegynte.await() + varslerSomBeskjed.await()
    }

    private suspend fun fetchActiveFromDigiSosIfEnabled(user: AuthenticatedUser) =
        if (unleashService.digiSosPaabegynteEnabled(user)) {
            log.info("Henter aktive påbegynte fra Digisos")
            digiSosService.getPaabegynteActive(user)
        } else {
            MultiSourceResult.createEmptyResult()
        }

    private suspend fun fetchActiveFromVarselinnboksIfEnabled(user: AuthenticatedUser) =
        if (unleashService.mergeBeskjedVarselEnabled(user)) {
            varselService.getActiveVarselEvents(user)

        } else {
            MultiSourceResult.createEmptyResult()
        }

    suspend fun getInactiveEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> = withContext(Dispatchers.IO) {
        val beskjeder = async {
            beskjedService.getInactiveBeskjedEvents(user)
        }

        val paabegynte = async {
            fetchInactiveFromDigiSosIfEnabled(user)
        }

        val varslerSomBeskjed = async {
            fetchInactiveFromVarselinnboksIfEnabled(user)
        }

        beskjeder.await() + paabegynte.await() + varslerSomBeskjed.await()
    }

    private suspend fun fetchInactiveFromDigiSosIfEnabled(user: AuthenticatedUser) =
        if (unleashService.digiSosPaabegynteEnabled(user)) {
            digiSosService.getPaabegynteInactive(user)
        } else {
            MultiSourceResult.createEmptyResult()
        }

    private suspend fun fetchInactiveFromVarselinnboksIfEnabled(user: AuthenticatedUser) =
        if (unleashService.mergeBeskjedVarselEnabled(user)) {
            log.info("Henter inaktive påbegynte for Digisos")
            varselService.getInactiveVarselEvents(user)
        } else {
            MultiSourceResult.createEmptyResult()
        }

}

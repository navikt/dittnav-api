package no.nav.personbruker.dittnav.api.beskjed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.api.varsel.VarselService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class BeskjedMergerService(
    private val beskjedService: BeskjedService,
    private val varselService: VarselService,
    private val digiSosService: DigiSosService,
    private val unleashService: UnleashService
) {

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
        if (unleashService.digiSosEnabled(user)) {
            digiSosService.getActiveEvents(user)
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
        if (unleashService.digiSosEnabled(user)) {
            digiSosService.getInactiveEvents(user)
        } else {
            MultiSourceResult.createEmptyResult()
        }

    private suspend fun fetchInactiveFromVarselinnboksIfEnabled(user: AuthenticatedUser) =
        if (unleashService.mergeBeskjedVarselEnabled(user)) {
            varselService.getInactiveVarselEvents(user)
        } else {
            MultiSourceResult.createEmptyResult()
        }

}

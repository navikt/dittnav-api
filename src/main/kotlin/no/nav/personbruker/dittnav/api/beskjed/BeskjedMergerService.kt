package no.nav.personbruker.dittnav.api.beskjed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.unleash.UnleashService


class BeskjedMergerService(
    private val beskjedService: BeskjedService,
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

        beskjeder.await() + paabegynte.await()
    }

    private suspend fun fetchActiveFromDigiSosIfEnabled(user: AuthenticatedUser) =
        if (unleashService.digiSosPaabegynteEnabled(user)) {
            digiSosService.getPaabegynteActive(user)
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

        beskjeder.await() + paabegynte.await()
    }

    private suspend fun fetchInactiveFromDigiSosIfEnabled(user: AuthenticatedUser) =
        if (unleashService.digiSosPaabegynteEnabled(user)) {
            digiSosService.getPaabegynteInactive(user)
        } else {
            MultiSourceResult.createEmptyResult()
        }

}

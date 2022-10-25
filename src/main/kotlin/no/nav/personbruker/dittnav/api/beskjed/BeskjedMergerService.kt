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
    private val digiSosService: DigiSosService
) {

    suspend fun getActiveEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> = withContext(Dispatchers.IO) {
        val beskjeder = async {
            beskjedService.getActiveBeskjedEvents(user)
        }

        val paabegynte = async {
            digiSosService.getPaabegynteActive(user)
        }

        beskjeder.await() + paabegynte.await()
    }


    suspend fun getInactiveEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> = withContext(Dispatchers.IO) {
        val beskjeder = async {
            beskjedService.getInactiveBeskjedEvents(user)
        }

        val paabegynte = async {
            digiSosService.getPaabegynteInactive(user)
        }

        beskjeder.await() + paabegynte.await()
    }

}

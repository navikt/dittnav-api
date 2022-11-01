package no.nav.personbruker.dittnav.api.beskjed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.digisos.DigiSosConsumer



class BeskjedMergerService(
    private val beskjedConsumer: BeskjedConsumer,
    private val digiSosConsumer: DigiSosConsumer
) {

    suspend fun getActiveEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> = withContext(Dispatchers.IO) {
        val beskjeder = async {
            beskjedConsumer.getActiveBeskjedEvents(user)
        }

        val paabegynte = async {
            digiSosConsumer.getPaabegynteActive(user)
        }

        beskjeder.await() + paabegynte.await()
    }


    suspend fun getInactiveEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> = withContext(Dispatchers.IO) {
        val beskjeder = async {
            beskjedConsumer.getInactiveBeskjedEvents(user)
        }

        val paabegynte = async {
            digiSosConsumer.getPaabegynteInactive(user)
        }

        beskjeder.await() + paabegynte.await()
    }

}

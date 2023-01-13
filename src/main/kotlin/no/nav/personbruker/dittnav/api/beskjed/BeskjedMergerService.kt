package no.nav.personbruker.dittnav.api.beskjed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.authentication.AuthenticatedUser
import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.digisos.DigiSosConsumer


class BeskjedMergerService(
    private val beskjedConsumer: BeskjedConsumer,
    private val digiSosConsumer: DigiSosConsumer
) {

    suspend fun getActiveEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> =
        withContext(Dispatchers.IO) {
            beskjedConsumer.getActiveBeskjedEvents(user)
        }


    suspend fun getInactiveEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> =
        withContext(Dispatchers.IO) {
            beskjedConsumer.getInactiveBeskjedEvents(user)
        }
}

package no.nav.personbruker.dittnav.api.beskjed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.varsel.VarselService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory

class MergeBeskjedMedVarselService(
    private val beskjedService: BeskjedService,
    private val varselService: VarselService
) {

    private val log = LoggerFactory.getLogger(MergeBeskjedMedVarselService::class.java)

    suspend fun getActiveEvents(user: AuthenticatedUser): BeskjedResult = withContext(Dispatchers.IO) {
        val beskjeder = async {
            beskjedService.getActiveBeskjedEvents(user)
        }

        val varslerSomBeskjed = async {
            varselService.getActiveVarselEvents(user)
        }

        beskjeder.await() + varslerSomBeskjed.await()
    }

    suspend fun getInactiveEvents(user: AuthenticatedUser): BeskjedResult = withContext(Dispatchers.IO) {
        val beskjeder = async {
            beskjedService.getInactiveBeskjedEvents(user)
        }

        val varslerSomBeskjed = async {
            varselService.getInactiveVarselEvents(user)
        }

        beskjeder.await() + varslerSomBeskjed.await()
    }

}

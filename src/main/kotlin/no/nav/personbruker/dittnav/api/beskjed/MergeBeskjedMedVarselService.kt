package no.nav.personbruker.dittnav.api.beskjed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.api.common.ConsumeEventException
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import no.nav.personbruker.dittnav.api.varsel.VarselService
import org.slf4j.LoggerFactory

class MergeBeskjedMedVarselService(
        private val beskjedService: BeskjedService,
        private val varselService: VarselService
) {

    private val log = LoggerFactory.getLogger(MergeBeskjedMedVarselService::class.java)

    suspend fun getActiveEvents(innloggetBruker: InnloggetBruker): BeskjedResult {
        return try {
            withContext(Dispatchers.IO) {
                val beskjeder = async {
                    beskjedService.getActiveBeskjedEvents(innloggetBruker)
                }

                val varslerSomBeskjed = async {
                    varselService.getActiveVarselEvents(innloggetBruker)
                }

                beskjeder.await() + varslerSomBeskjed.await()
            }
        } catch (e: Exception) {
            throw ConsumeEventException(
                    "Uventet feil ved sammenslåing av aktive beskjeder og varsler.", e
            )
        }
    }

    suspend fun getInactiveEvents(innloggetBruker: InnloggetBruker): BeskjedResult {
        return try {
            withContext(Dispatchers.IO) {
                val beskjeder = async {
                    beskjedService.getInactiveBeskjedEvents(innloggetBruker)
                }

                val varslerSomBeskjed = async {
                    varselService.getInactiveVarselEvents(innloggetBruker)
                }

                beskjeder.await() + varslerSomBeskjed.await()
            }
        } catch (e: Exception) {
            throw ConsumeEventException(
                    "Uventet feil ved sammenslåing av inaktive beskjeder og varsler. Minst en av kildene feilet.", e
            )
        }
    }

}

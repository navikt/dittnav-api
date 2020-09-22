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

    suspend fun getActiveEvents(innloggetBruker: InnloggetBruker): List<BeskjedDTO> {
        return try {
            withContext(Dispatchers.IO) {
                val beskjeder = async {
                    beskjedService.getActiveBeskjedEvents(innloggetBruker)
                }

                val varslerSomBeskjed = async {
                    varselService.getActiveVarselEvents(innloggetBruker)
                }

                val allActive = mutableListOf<BeskjedDTO>()
                allActive.addAll(beskjeder.await())
                allActive.addAll(varslerSomBeskjed.await())
                allActive
            }
        } catch (e: Exception) {
            throw ConsumeEventException(
                "Uventet feil ved sammenslåing av aktive beskjeder og varsler. Minst en av kildene feilet.", e
            )
        }
    }

    suspend fun getInactiveEvents(innloggetBruker: InnloggetBruker): List<BeskjedDTO> {
        return try {
            withContext(Dispatchers.IO) {
                val beskjeder = async {
                    beskjedService.getInactiveBeskjedEvents(innloggetBruker)
                }

                val varslerSomBeskjed = async {
                    varselService.getInactiveVarselEvents(innloggetBruker)
                }

                val allInactive = mutableListOf<BeskjedDTO>()
                allInactive.addAll(beskjeder.await())
                allInactive.addAll(varslerSomBeskjed.await())

                return@withContext allInactive
            }
        } catch (e: Exception) {
            throw ConsumeEventException(
                "Uventet feil ved sammenslåing av inaktive beskjeder og varsler. Minst en av kildene feilet.", e
            )
        }
    }

}

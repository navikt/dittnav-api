package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import org.slf4j.LoggerFactory

class BeskjedVarselSwitcher(
    private val beskjedService: BeskjedService,
    private val beskjedMedVarselService: MergeBeskjedMedVarselService,
    private val includeVarsel: Boolean
) {

    private val log = LoggerFactory.getLogger(BeskjedService::class.java)

    init {
        if (includeVarsel) {
            log.info("Henting av Beskjeder og Varsler er aktivert.")

        } else {
            log.info("Kun henting av Beskjeder er aktivert.")
        }
    }

    suspend fun getActiveEvents(innloggetBruker: InnloggetBruker): List<BeskjedDTO> {
        return if (includeVarsel) {
            beskjedMedVarselService.getActiveEvents(innloggetBruker)

        } else {
            beskjedService.getActiveBeskjedEvents(innloggetBruker)
        }
    }

    suspend fun getInactiveEvents(innloggetBruker: InnloggetBruker): List<BeskjedDTO> {
        return if (includeVarsel) {
            beskjedMedVarselService.getInactiveEvents(innloggetBruker)

        } else {
            beskjedService.getInactiveBeskjedEvents(innloggetBruker)
        }
    }

}

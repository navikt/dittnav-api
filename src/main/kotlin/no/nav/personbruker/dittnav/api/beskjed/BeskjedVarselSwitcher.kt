package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import org.slf4j.LoggerFactory

class BeskjedVarselSwitcher(
    private val beskjedService: BeskjedService,
    private val beskjedMedVarselService: MergeBeskjedMedVarselService,
    private val includeVarsel: Boolean
) {

    private val log = LoggerFactory.getLogger(BeskjedVarselSwitcher::class.java)

    init {
        if (includeVarsel) {
            log.info("Henting av Beskjeder og Varsler er aktivert.")

        } else {
            log.info("Kun henting av Beskjeder er aktivert.")
        }
    }

    suspend fun getActiveEvents(user: AuthenticatedUser): BeskjedResult {
        return if (includeVarsel) {
            beskjedMedVarselService.getActiveEvents(user)

        } else {
            beskjedService.getActiveBeskjedEvents(user)
        }
    }

    suspend fun getInactiveEvents(user: AuthenticatedUser): BeskjedResult {
        return if (includeVarsel) {
            beskjedMedVarselService.getInactiveEvents(user)

        } else {
            beskjedService.getInactiveBeskjedEvents(user)
        }
    }

}

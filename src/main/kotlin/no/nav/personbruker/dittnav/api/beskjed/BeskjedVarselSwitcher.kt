package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.common.logging.util.logger
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class BeskjedVarselSwitcher(
    private val beskjedService: BeskjedService,
    private val beskjedMedVarselService: MergeBeskjedMedVarselService,
    private val unleashService: UnleashService
) {

    suspend fun getActiveEvents(user: AuthenticatedUser): BeskjedResult {
        return if (unleashService.mergeBeskjedVarselEnabled(user)) {
            logger.info("Viser aktive beskjeder og varsler.")
            beskjedMedVarselService.getActiveEvents(user)

        } else {
            logger.info("Viser kun aktive beskjeder.")
            beskjedService.getActiveBeskjedEvents(user)
        }
    }

    suspend fun getInactiveEvents(user: AuthenticatedUser): BeskjedResult {
        return if (unleashService.mergeBeskjedVarselEnabled(user)) {
            logger.info("Viser inaktive beskjeder og varsler.")
            beskjedMedVarselService.getInactiveEvents(user)

        } else {
            logger.info("Viser kun inaktive beskjeder.")
            beskjedService.getInactiveBeskjedEvents(user)
        }
    }

}

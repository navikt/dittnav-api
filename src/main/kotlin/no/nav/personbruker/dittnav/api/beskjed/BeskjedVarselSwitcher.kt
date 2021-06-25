package no.nav.personbruker.dittnav.api.beskjed

import no.nav.personbruker.dittnav.api.common.MultiSourceResult
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class BeskjedVarselSwitcher(
    private val beskjedService: BeskjedService,
    private val beskjedMedVarselService: MergeBeskjedMedVarselService,
    private val unleashService: UnleashService
) {

    suspend fun getActiveEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        return if (unleashService.mergeBeskjedVarselEnabled(user)) {
            beskjedMedVarselService.getActiveEvents(user)

        } else {
            beskjedService.getActiveBeskjedEvents(user)
        }
    }

    suspend fun getInactiveEvents(user: AuthenticatedUser): MultiSourceResult<BeskjedDTO, KildeType> {
        return if (unleashService.mergeBeskjedVarselEnabled(user)) {
            beskjedMedVarselService.getInactiveEvents(user)

        } else {
            beskjedService.getInactiveBeskjedEvents(user)
        }
    }

}

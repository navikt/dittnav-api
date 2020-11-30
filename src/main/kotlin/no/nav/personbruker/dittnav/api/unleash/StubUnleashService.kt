package no.nav.personbruker.dittnav.api.unleash

import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class StubUnleashService(private val mergeVarselEnabled: Boolean): UnleashService {

    override suspend fun mergeVarselEnabled(user: AuthenticatedUser): Boolean {
        return mergeVarselEnabled
    }
}
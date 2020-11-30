package no.nav.personbruker.dittnav.api.unleash

import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

interface UnleashService {
    suspend fun mergeVarselEnabled(user: AuthenticatedUser): Boolean
}
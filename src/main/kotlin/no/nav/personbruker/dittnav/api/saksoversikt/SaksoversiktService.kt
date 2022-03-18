package no.nav.personbruker.dittnav.api.saksoversikt

import no.nav.personbruker.dittnav.api.tokenx.AccessToken
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class SaksoversiktService(
    private val saksoversiktConsumer: SaksoversiktConsumer,
    private val paabegynteSoknaderTransformer: PaabegynteSoknaderTransformer
) {

    suspend fun getSakstema(user: AuthenticatedUser): SakstemaWrapper {
        val token = AccessToken(user.token)

        val sakstema = saksoversiktConsumer.getSakstema(token)

        return SakstemaTransformer.toInternal(sakstema)
    }

    suspend fun getPaabegynte(user: AuthenticatedUser): PaabegynteSoknader {
        val token = AccessToken(user.token)

        val paabegynteSoknader = saksoversiktConsumer.getPaabegynteSoknader(token)

        return paabegynteSoknaderTransformer.toInternal(paabegynteSoknader)
    }
}

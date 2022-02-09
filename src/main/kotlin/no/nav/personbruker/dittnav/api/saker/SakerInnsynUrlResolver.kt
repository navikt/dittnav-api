package no.nav.personbruker.dittnav.api.saker

import org.slf4j.LoggerFactory
import java.net.URL

class SakerInnsynUrlResolver(
    isRunningInProd: Boolean
) {

    private val log = LoggerFactory.getLogger(SakerInnsynUrlResolver::class.java)

    private val mineSakerUrl: URL
    private val saksoversiktUrl: URL

    init {
        if (isRunningInProd) {
            mineSakerUrl = URL("https://person.nav.no/mine-saker")
            saksoversiktUrl = URL("https://tjenester.nav.no/saksoversikt")
            log.info("SakerInnsynUrlResolver: running in prod mode")

        } else {
            mineSakerUrl = URL("https://person.dev.nav.no/mine-saker")
            saksoversiktUrl = URL("https://tjenester-q1.nav.no/saksoversikt")
            log.info("SakerInnsynUrlResolver: running in dev mode")
        }
    }

    fun getMineSakerUrl() : URL {
        return mineSakerUrl
    }

    fun getSaksoversiktUrl() : URL {
        return saksoversiktUrl
    }

}

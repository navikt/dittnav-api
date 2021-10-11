package no.nav.personbruker.dittnav.api.legacy.saksoversikt

import no.nav.personbruker.dittnav.api.config.NaisEnvironment
import no.nav.personbruker.dittnav.api.legacy.log
import java.net.URL

val innsynsUrlResolverSingleton = if (NaisEnvironment.isRunningInProd()) {
    log.info("InnsynsUrlResolver konfigurert for PROD")
    InnsynsUrlResolver(true)

} else {
    log.info("InnsynsUrlResolver konfigurert for DEV")
    InnsynsUrlResolver(false)
}

class InnsynsUrlResolver(
    isRunningInProd : Boolean
) {

    private val temaspesifikkeLenker : Map<String, URL>
    private val generellLenke : URL

    init {
        if(isRunningInProd) {
            temaspesifikkeLenker = innsynslenkerProd
            generellLenke = generellInnsynslenkeProd

        } else {
            temaspesifikkeLenker = innsynslenkerDev
            generellLenke = generellInnsynslenkeDev
        }
    }

    fun urlFor(kode : String) : URL {
        return temaspesifikkeLenker.getOrDefault(kode, URL("$generellLenke$kode"))
    }

}

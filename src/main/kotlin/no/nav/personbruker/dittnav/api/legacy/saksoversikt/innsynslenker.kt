package no.nav.personbruker.dittnav.api.legacy.saksoversikt

import java.net.URL

val generellInnsynslenkeDev = URL("https://tjenester-q1.nav.no/saksoversikt/tema/")
val innsynslenkerDev : Map<String, URL> = mapOf(
    "DAG" to URL("https://arbeid.dev.nav.no/arbeid/dagpenger/mine-dagpenger"),
    "HJE" to URL("https://hjelpemidler.dev.nav.no/hjelpemidler/dinehjelpemidler"),
    "KOM" to URL("https://www-q1.dev.nav.no/sosialhjelp/innsyn"),
    "SYK" to URL("https://www-gcp.dev.nav.no/syk/sykmeldinger/")
)

val generellInnsynslenkeProd = URL("https://tjenester.nav.no/saksoversikt/tema/")
val innsynslenkerProd : Map<String, URL> = mapOf(
    "DAG" to URL("https://www.nav.no/arbeid/dagpenger/mine-dagpenger"),
    "HJE" to URL("https://www.nav.no/hjelpemidler/dinehjelpemidler"),
    "KOM" to URL("https://www.nav.no/sosialhjelp/innsyn"),
    "SYK" to URL("https://www.nav.no/syk/sykmeldinger")
)

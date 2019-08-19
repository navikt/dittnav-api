package no.nav.personbruker.dittnav.api

import no.nav.personbruker.dittnav.api.config.Server
import no.nav.personbruker.dittnav.api.config.Environment

fun main() {
    val environment = Environment()
    Server.configure(environment).start()
}

package no.nav.personbruker.dittnav.proxy

import no.nav.personbruker.dittnav.proxy.config.Environment
import no.nav.personbruker.dittnav.proxy.config.Server

fun main() {
    val environment = Environment()
    Server.configure(environment).start()
}

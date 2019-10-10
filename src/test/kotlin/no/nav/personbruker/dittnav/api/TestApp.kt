package no.nav.personbruker.dittnav.api

import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.Server

fun main() {
    val environment = Environment(isDev = true)
    Server.configure(HttpClientMock().client, environment).start()
}

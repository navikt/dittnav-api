package no.nav.personbruker.dittnav.api

import no.nav.personbruker.dittnav.api.config.Server

fun main() {
    Server.configure(HttpClientMock().client).start()
}

package no.nav.personbruker.dittnav.api

import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.Server
import no.nav.personbruker.dittnav.api.config.HttpClient

fun main() {
    Server.configure(HttpClient().client, Environment()).start()
}

package no.nav.personbruker.dittnav.api.config

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonService
import no.nav.personbruker.dittnav.api.brukernotifikasjon.brukernotifikasjoner
import no.nav.personbruker.dittnav.api.health.healthApi
import no.nav.personbruker.dittnav.api.beskjed.BeskjedConsumer
import no.nav.personbruker.dittnav.api.beskjed.BeskjedService
import no.nav.personbruker.dittnav.api.innboks.InnboksConsumer
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.legacy.*
import no.nav.personbruker.dittnav.api.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService

import no.nav.security.token.support.ktor.tokenValidationSupport

@KtorExperimentalAPI
fun Application.mainModule() {
    val environment = Environment()

    DefaultExports.initialize()

    val legacyConsumer = LegacyConsumer(HttpClientBuilder, environment)
    val oppgaveService = OppgaveService(OppgaveConsumer(HttpClientBuilder, environment))
    val beskjedService = BeskjedService(BeskjedConsumer(HttpClientBuilder, environment))
    val innboksService = InnboksService(InnboksConsumer(HttpClientBuilder, environment))
    val brukernotifikasjonService = BrukernotifikasjonService(oppgaveService, beskjedService, innboksService)

    install(DefaultHeaders)

    val config = this.environment.config

    install(Authentication) {
        tokenValidationSupport(config = config)
    }

    install(ContentNegotiation) {
        jackson {
            enableDittNavJsonConfig()
        }
    }

    routing {
        healthApi(environment)
        authenticate {
            legacyMeldinger(legacyConsumer)
            legacyPabegynte(legacyConsumer)
            legacyMeldekortinfo(legacyConsumer)
            legacyPersonident(legacyConsumer)
            legacyPersonnavn(legacyConsumer)
            legacySakstema(legacyConsumer)
            legacyOppfolging(legacyConsumer)
            brukernotifikasjoner(brukernotifikasjonService)
        }
    }
}

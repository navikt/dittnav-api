package no.nav.personbruker.dittnav.api.config

import com.fasterxml.jackson.databind.SerializationFeature
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
import no.nav.personbruker.dittnav.api.informasjon.InformasjonConsumer
import no.nav.personbruker.dittnav.api.informasjon.InformasjonService
import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer
import no.nav.personbruker.dittnav.api.legacy.legacyMeldinger
import no.nav.personbruker.dittnav.api.legacy.legacyPabegynte
import no.nav.personbruker.dittnav.api.legacy.legacyPersoninfo
import no.nav.personbruker.dittnav.api.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import no.nav.security.token.support.ktor.tokenValidationSupport

@KtorExperimentalAPI
fun Application.mainModule() {
    val environment = Environment()

    DefaultExports.initialize()

    val legacyConsumer = LegacyConsumer(HttpClientBuilder, environment)
    val oppgaveService = OppgaveService(OppgaveConsumer(HttpClientBuilder, environment))
    val informasjonService = InformasjonService(InformasjonConsumer(HttpClientBuilder, environment))
    val brukernotifikasjonService = BrukernotifikasjonService(oppgaveService, informasjonService)

    install(DefaultHeaders)

    val config = this.environment.config

    install(Authentication) {
        tokenValidationSupport(config = config)
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        healthApi()
        authenticate {
            legacyMeldinger(legacyConsumer)
            legacyPabegynte(legacyConsumer)
            legacyPersoninfo(legacyConsumer)
            brukernotifikasjoner(brukernotifikasjonService)
        }
    }
}

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
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.dittnav.api.event.EventConsumer
import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer
import no.nav.personbruker.dittnav.api.legacy.legacyMeldinger
import no.nav.personbruker.dittnav.api.legacy.legacyPabegynte
import no.nav.personbruker.dittnav.api.legacy.legacyPersoninfo
import no.nav.personbruker.dittnav.api.melding.MeldingService
import no.nav.personbruker.dittnav.api.melding.meldinger
import no.nav.security.token.support.ktor.tokenValidationSupport

fun Application.mainModule() {
    val environment = Environment()
    val httpClient = HttpClient().client

    DefaultExports.initialize()

    val legacyConsumer = LegacyConsumer(httpClient, environment)
    val meldingService = MeldingService(EventConsumer(httpClient, environment))

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
            meldinger(meldingService)
        }
    }

}

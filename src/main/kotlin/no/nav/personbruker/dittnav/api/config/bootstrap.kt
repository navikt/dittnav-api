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
import no.nav.personbruker.dittnav.api.event.EventConsumer
import no.nav.personbruker.dittnav.api.legacy.*
import no.nav.personbruker.dittnav.api.melding.MeldingService
import no.nav.personbruker.dittnav.api.melding.meldinger
import no.nav.security.token.support.ktor.tokenValidationSupport

@KtorExperimentalAPI
fun Application.mainModule() {
    val environment = Environment()

    DefaultExports.initialize()

    val legacyConsumer = LegacyConsumer(HttpClientBuilder, environment)
    val meldingService = MeldingService(EventConsumer(HttpClientBuilder, environment))

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
            legacyMeldekortinfo(legacyConsumer)
            legacyPersonident(legacyConsumer)
            legacyPersonnavn(legacyConsumer)
            legacySakstema(legacyConsumer)
            legacyOppfolging(legacyConsumer)

            meldinger(meldingService)
        }
    }

}

package no.nav.personbruker.dittnav.api.config

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.dittnav.api.beskjed.beskjed
import no.nav.personbruker.dittnav.api.brukernotifikasjon.brukernotifikasjoner
import no.nav.personbruker.dittnav.api.done.doneApi
import no.nav.personbruker.dittnav.api.health.authenticationCheck
import no.nav.personbruker.dittnav.api.health.healthApi
import no.nav.personbruker.dittnav.api.innboks.innboks
import no.nav.personbruker.dittnav.api.legacy.legacyApi
import no.nav.personbruker.dittnav.api.oppgave.oppgave
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import no.nav.personbruker.dittnav.common.security.AuthenticatedUserFactory
import no.nav.security.token.support.ktor.tokenValidationSupport
import org.slf4j.LoggerFactory
import java.time.Instant

val log = LoggerFactory.getLogger(ApplicationContext::class.java)

@KtorExperimentalAPI
fun Application.mainModule(appContext: ApplicationContext = ApplicationContext()) {

    DefaultExports.initialize()

    install(DefaultHeaders)

    install(CORS) {
        host(appContext.environment.corsAllowedOrigins, schemes = listOf(appContext.environment.corsAllowedSchemes))
        allowCredentials = true
        header(HttpHeaders.ContentType)
    }

    val config = this.environment.config

    install(Authentication) {
        tokenValidationSupport(config = config)
    }

    install(ContentNegotiation) {
        json(no.nav.personbruker.dittnav.api.config.json())
    }

    routing {
        healthApi(appContext.httpClient, appContext.environment)
        authenticate {
            intercept(ApplicationCallPipeline.Call) {
                if (authenticatedUser.isTokenExpired()) {
                    val delta = authenticatedUser.tokenExpirationTime.epochSecond - Instant.now().epochSecond
                    log.info("Mottok kall fra en bruker med et utløpt token. Delta: $delta sekunder, $authenticatedUser")
                }
            }

            legacyApi(appContext.legacyConsumer)
            oppgave(appContext.oppgaveService)
            beskjed(appContext.beskjedVarselSwitcher)
            innboks(appContext.innboksService)
            brukernotifikasjoner(appContext.brukernotifikasjonService)
            authenticationCheck()
            doneApi(appContext.doneProducer)
        }

        configureShutdownHook(listOf(appContext.httpClient, appContext.httpClientIgnoreUnknownKeys))
    }
}

private fun Application.configureShutdownHook(httpClients: List<HttpClient>) {
    environment.monitor.subscribe(ApplicationStopping) {
        httpClients.forEach { httpClient -> httpClient.close() }
    }
}

val PipelineContext<Unit, ApplicationCall>.authenticatedUser: AuthenticatedUser
    get() = AuthenticatedUserFactory.createNewAuthenticatedUser(call)

package no.nav.personbruker.dittnav.api.config

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.metrics.micrometer.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.util.pipeline.*
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.dittnav.api.beskjed.beskjed
import no.nav.personbruker.dittnav.api.digisos.digiSos
import no.nav.personbruker.dittnav.api.done.doneApi
import no.nav.personbruker.dittnav.api.health.authenticationCheck
import no.nav.personbruker.dittnav.api.health.healthApi
import no.nav.personbruker.dittnav.api.innboks.innboks
import no.nav.personbruker.dittnav.api.meldekort.meldekortApi
import no.nav.personbruker.dittnav.api.mininnboks.ubehandledeMeldingerApi
import no.nav.personbruker.dittnav.api.oppfolging.oppfolgingApi
import no.nav.personbruker.dittnav.api.oppgave.oppgave
import no.nav.personbruker.dittnav.api.personalia.personalia
import no.nav.personbruker.dittnav.api.saker.saker
import no.nav.personbruker.dittnav.api.saksoversikt.saksoversiktApi
import no.nav.personbruker.dittnav.api.unleash.unleash
import no.nav.personbruker.dittnav.common.security.AuthenticatedUser
import no.nav.personbruker.dittnav.common.security.AuthenticatedUserFactory
import no.nav.security.token.support.ktor.tokenValidationSupport
import org.slf4j.LoggerFactory
import java.time.Instant

val log = LoggerFactory.getLogger(ApplicationContext::class.java)

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

    install(MicrometerMetrics) {
        registry = appContext.appMicrometerRegistry
    }

    routing {
        healthApi(appContext.dependencyPinger, appContext.appMicrometerRegistry)
        authenticate {
            intercept(ApplicationCallPipeline.Call) {
                if (authenticatedUser.isTokenExpired()) {
                    val delta = authenticatedUser.tokenExpirationTime.epochSecond - Instant.now().epochSecond
                    log.info("Mottok kall fra en bruker med et utløpt token. Delta: $delta sekunder, $authenticatedUser")
                }
            }

            meldekortApi(appContext.meldekortService)
            oppfolgingApi(appContext.oppfolgingService)
            ubehandledeMeldingerApi(appContext.ubehandledeMeldingerService)
            saksoversiktApi(appContext.saksoversiktService)

            oppgave(appContext.oppgaveMergerService)
            beskjed(appContext.beskjedMergerService)
            innboks(appContext.innboksService)
            saker(appContext.sakerService)
            personalia(appContext.personaliaService)
            unleash(appContext.unleashService)
            if(appContext.environment.isRunningInDev) {
                digiSos(appContext.digiSosService)
            }
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

suspend fun PipelineContext<Unit, ApplicationCall>.executeOnUnexpiredTokensOnly(block: suspend () -> Unit) {
    if (authenticatedUser.isTokenExpired()) {
        val delta = authenticatedUser.tokenExpirationTime.epochSecond - Instant.now().epochSecond
        val msg = "Mottok kall fra en bruker med et utløpt token, avviser request-en med en 401-respons. " +
                "Tid siden tokenet løp ut: $delta sekunder, $authenticatedUser"
        log.info(msg)
        call.respond(HttpStatusCode.Unauthorized)

    } else {
        block.invoke()
    }
}

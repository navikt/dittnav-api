package no.nav.personbruker.dittnav.api.config

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.dittnav.api.beskjed.*
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonConsumer
import no.nav.personbruker.dittnav.api.brukernotifikasjon.BrukernotifikasjonService
import no.nav.personbruker.dittnav.api.brukernotifikasjon.brukernotifikasjoner
import no.nav.personbruker.dittnav.api.common.InnloggetBruker
import no.nav.personbruker.dittnav.api.common.InnloggetBrukerFactory
import no.nav.personbruker.dittnav.api.done.DoneProducer
import no.nav.personbruker.dittnav.api.done.doneApi
import no.nav.personbruker.dittnav.api.health.authenticationCheck
import no.nav.personbruker.dittnav.api.health.healthApi
import no.nav.personbruker.dittnav.api.innboks.InnboksConsumer
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.innboks.innboks
import no.nav.personbruker.dittnav.api.legacy.LegacyConsumer
import no.nav.personbruker.dittnav.api.legacy.legacyApi
import no.nav.personbruker.dittnav.api.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import no.nav.personbruker.dittnav.api.oppgave.oppgave
import no.nav.personbruker.dittnav.api.varsel.VarselConsumer
import no.nav.personbruker.dittnav.api.varsel.VarselService
import no.nav.security.token.support.ktor.tokenValidationSupport

@KtorExperimentalAPI
fun Application.mainModule() {
    val environment = Environment()

    DefaultExports.initialize()

    val httpClient = HttpClientBuilder.build()

    val legacyConsumer = LegacyConsumer(httpClient, environment.legacyApiURL)
    val oppgaveConsumer = OppgaveConsumer(httpClient, environment.eventHandlerURL)
    val beskjedConsumer = BeskjedConsumer(httpClient, environment.eventHandlerURL)
    val innboksConsumer = InnboksConsumer(httpClient, environment.eventHandlerURL)
    val brukernotifikasjonConsumer = BrukernotifikasjonConsumer(httpClient, environment.eventHandlerURL)
    val varselConsumer = VarselConsumer(httpClient, environment.legacyApiURL)

    val doneProducer = DoneProducer(httpClient, environment.eventHandlerURL)

    val oppgaveService = OppgaveService(oppgaveConsumer)
    val beskjedService = BeskjedService(beskjedConsumer)
    val innboksService = InnboksService(innboksConsumer)
    val brukernotifikasjonService = BrukernotifikasjonService(brukernotifikasjonConsumer)
    val varselService = VarselService(varselConsumer)
    val mergeBeskjedMedVarselService = MergeBeskjedMedVarselService(beskjedService, varselService)
    val enableVarsel = environment.includeVarselinnboks && environment.isRunningInDev
    val beskjedVarselSwitcher = BeskjedVarselSwitcher(
        beskjedService,
        mergeBeskjedMedVarselService,
        enableVarsel
    )

    install(DefaultHeaders)

    install(CORS) {
        host(environment.corsAllowedOrigins, schemes = listOf(environment.corsAllowedSchemes))
        allowCredentials = true
        header(HttpHeaders.ContentType)
    }

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
            legacyApi(legacyConsumer)
            oppgave(oppgaveService)
            beskjed(beskjedVarselSwitcher)
            innboks(innboksService)
            brukernotifikasjoner(brukernotifikasjonService)
            authenticationCheck()
            doneApi(doneProducer)
        }

        configureShutdownHook(httpClient)
    }
}

private fun Application.configureShutdownHook(httpClient: HttpClient) {
    environment.monitor.subscribe(ApplicationStopping) {
        httpClient.close()
    }
}

val PipelineContext<Unit, ApplicationCall>.innloggetBruker: InnloggetBruker
    get() = InnloggetBrukerFactory.createNewInnloggetBruker(call.authentication.principal())

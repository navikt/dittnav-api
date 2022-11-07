package no.nav.personbruker.dittnav.api.config

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import no.finn.unleash.DefaultUnleash
import no.finn.unleash.FakeUnleash
import no.finn.unleash.Unleash
import no.finn.unleash.util.UnleashConfig
import no.nav.personbruker.dittnav.api.beskjed.BeskjedConsumer
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.beskjed.BeskjedService
import no.nav.personbruker.dittnav.api.digisos.DigiSosClient
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.done.DoneProducer
import no.nav.personbruker.dittnav.api.innboks.InnboksConsumer
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.meldekort.MeldekortConsumer
import no.nav.personbruker.dittnav.api.meldekort.MeldekortService
import no.nav.personbruker.dittnav.api.meldekort.MeldekortTokendings
import no.nav.personbruker.dittnav.api.oppfolging.OppfolgingConsumer
import no.nav.personbruker.dittnav.api.oppfolging.OppfolgingService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.api.personalia.PersonaliaConsumer
import no.nav.personbruker.dittnav.api.personalia.PersonaliaService
import no.nav.personbruker.dittnav.api.personalia.PersonaliaTokendings
import no.nav.personbruker.dittnav.api.saker.MineSakerConsumer
import no.nav.personbruker.dittnav.api.saker.MineSakerTokendings
import no.nav.personbruker.dittnav.api.saker.SakerService
import no.nav.personbruker.dittnav.api.unleash.ByEnvironmentStrategy
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder
import java.net.URL

class ApplicationContext {

    val environment = Environment()
    val httpClient = HttpClientBuilder.build()

    private val tokendingsService = TokendingsServiceBuilder.buildTokendingsService()
    private val eventhandlerTokendings = EventhandlerTokendings(tokendingsService, environment.eventhandlerClientId)
    private val mineSakerTokendings = MineSakerTokendings(tokendingsService, environment.mineSakerApiClientId)
    private val personaliaTokendings = PersonaliaTokendings(tokendingsService, environment.pdlClientId)

    private val oppgaveConsumer = OppgaveConsumer(httpClient, environment.eventHandlerURL)
    private val beskjedConsumer = BeskjedConsumer(httpClient, environment.eventHandlerURL)
    private val innboksConsumer = InnboksConsumer(httpClient, environment.eventHandlerURL)
    private val mineSakerConsumer = MineSakerConsumer(httpClient, environment.sakerApiUrl)

    val doneProducer = DoneProducer(httpClient, eventhandlerTokendings, environment.eventHandlerURL)

    val unleashService = createUnleashService(environment)

    val oppgaveService = OppgaveService(oppgaveConsumer, eventhandlerTokendings)
    private val beskjedService = BeskjedService(beskjedConsumer, eventhandlerTokendings)
    val innboksService = InnboksService(innboksConsumer, eventhandlerTokendings)
    val sakerService = SakerService(mineSakerConsumer, environment.mineSakerURL, mineSakerTokendings)

    private val digiSosConsumer = DigiSosClient(httpClient, environment.digiSosSoknadBaseURL)
    val digiSosService = DigiSosService(digiSosConsumer)

    val beskjedMergerService = BeskjedMergerService(beskjedService, digiSosService, unleashService)

    private val personaliaConsumer = PersonaliaConsumer(GraphQLKtorClient(URL(environment.pdlUrl), httpClient), environment.pdlUrl)
    val personaliaService = PersonaliaService(personaliaConsumer, personaliaTokendings)

    private val meldekortConsumer = MeldekortConsumer(httpClient, environment.meldekortApiUrl)
    private val meldekortTokendings = MeldekortTokendings(tokendingsService, environment.meldekortClientId)
    val meldekortService = MeldekortService(meldekortConsumer, meldekortTokendings)

    private val oppfolgingConsumer = OppfolgingConsumer(httpClient, environment.oppfolgingApiUrl)
    val oppfolgingService = OppfolgingService(oppfolgingConsumer)

    private fun createUnleashService(environment: Environment): UnleashService {

        val unleashClient = if (environment.unleashApiUrl == "fake") {
            createFakeUnleashClient(environment)
        } else {
            createUnleashClient(environment)
        }

        return UnleashService(unleashClient)
    }

    private fun createUnleashClient(environment: Environment): Unleash {
        val unleashUrl = environment.unleashApiUrl

        val appName = "dittnav-api"
        val envContext = if (NaisEnvironment.isRunningInDev()) "dev" else "prod"

        val byEnvironment = ByEnvironmentStrategy(envContext)

        val config = UnleashConfig.builder()
            .appName(appName)
            .unleashAPI(unleashUrl)
            .build()

        return DefaultUnleash(config, byEnvironment)
    }

    private fun createFakeUnleashClient(environment: Environment): Unleash {
        return FakeUnleash().apply {
            if (environment.fakeUnleashIncludeDigiSos) {
                enable(UnleashService.digisosPaabegynteToggleName)
            } else {
                disable(UnleashService.digisosPaabegynteToggleName)
            }
        }
    }
}

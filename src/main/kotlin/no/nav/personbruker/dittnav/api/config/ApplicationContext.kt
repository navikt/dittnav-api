package no.nav.personbruker.dittnav.api.config

import no.nav.personbruker.dittnav.api.beskjed.BeskjedConsumer
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.digisos.DigiSosConsumer
import no.nav.personbruker.dittnav.api.digisos.DigiSosTokendings
import no.nav.personbruker.dittnav.api.done.DoneProducer
import no.nav.personbruker.dittnav.api.innboks.InnboksConsumer
import no.nav.personbruker.dittnav.api.meldekort.MeldekortConsumer
import no.nav.personbruker.dittnav.api.meldekort.MeldekortTokendings
import no.nav.personbruker.dittnav.api.oppfolging.OppfolgingConsumer
import no.nav.personbruker.dittnav.api.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.api.tokenx.EventhandlerTokendings
import no.nav.personbruker.dittnav.api.personalia.PersonaliaConsumer
import no.nav.personbruker.dittnav.api.personalia.PersonaliaTokendings
import no.nav.personbruker.dittnav.api.saker.MineSakerConsumer
import no.nav.personbruker.dittnav.api.saker.MineSakerTokendings
import no.nav.personbruker.dittnav.api.tokenx.EventaggregatorTokendings
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder

class ApplicationContext {

    val environment = Environment()
    val httpClient = HttpClientBuilder.build()

    private val tokendingsService = TokendingsServiceBuilder.buildTokendingsService()
    private val eventhandlerTokendings = EventhandlerTokendings(tokendingsService, environment.eventhandlerClientId)
    private val eventaggregatorTokendings = EventaggregatorTokendings(tokendingsService, environment.eventaggregatorClientId)
    private val mineSakerTokendings = MineSakerTokendings(tokendingsService, environment.mineSakerApiClientId)
    private val personaliaTokendings = PersonaliaTokendings(tokendingsService, environment.personaliaApiClientId)
    private val meldekortTokendings = MeldekortTokendings(tokendingsService, environment.meldekortClientId)
    private val digiSosTokendings = DigiSosTokendings(tokendingsService, environment.digiSosClientId)

    val oppgaveConsumer = OppgaveConsumer(httpClient, eventhandlerTokendings, environment.eventHandlerURL)
    private val beskjedConsumer = BeskjedConsumer(httpClient, eventhandlerTokendings, environment.eventHandlerURL)
    val innboksConsumer = InnboksConsumer(httpClient, eventhandlerTokendings, environment.eventHandlerURL)
    val mineSakerConsumer = MineSakerConsumer(
        client = httpClient,
        mineSakerUrl = environment.mineSakerURL,
        mineSakerTokendings = mineSakerTokendings,
        mineSakerApiURL =environment.mineSakerApiUrl
    )

    val doneProducer = DoneProducer(httpClient, eventaggregatorTokendings, environment.eventAggregatorURL)
    val digiSosConsumer = DigiSosConsumer(httpClient, digiSosTokendings, environment.digiSosSoknadBaseURL)
    val beskjedMergerService = BeskjedMergerService(beskjedConsumer, digiSosConsumer)
    val personaliaConsumer = PersonaliaConsumer(httpClient, personaliaTokendings, environment.personaliaApiUrl)
    val meldekortConsumer = MeldekortConsumer(httpClient, meldekortTokendings, environment.meldekortApiUrl)
    val oppfolgingConsumer = OppfolgingConsumer(httpClient, environment.oppfolgingApiUrl)
}

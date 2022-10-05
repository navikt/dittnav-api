package no.nav.personbruker.dittnav.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondBytes
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.TestApplicationBuilder
import io.ktor.util.reflect.instanceOf
import io.mockk.mockk
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import no.nav.personbruker.dittnav.api.beskjed.BeskjedMergerService
import no.nav.personbruker.dittnav.api.config.api
import no.nav.personbruker.dittnav.api.config.jsonConfig
import no.nav.personbruker.dittnav.api.digisos.DigiSosService
import no.nav.personbruker.dittnav.api.done.DoneProducer
import no.nav.personbruker.dittnav.api.innboks.InnboksService
import no.nav.personbruker.dittnav.api.meldekort.MeldekortService
import no.nav.personbruker.dittnav.api.oppfolging.OppfolgingService
import no.nav.personbruker.dittnav.api.oppgave.OppgaveService
import no.nav.personbruker.dittnav.api.personalia.PersonaliaService
import no.nav.personbruker.dittnav.api.saker.SakerService
import no.nav.personbruker.dittnav.api.unleash.UnleashService
import org.intellij.lang.annotations.Language
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private const val testIssuer = "test-issuer"
private val jwtStub = JwtStub(testIssuer)
internal val stubToken = jwtStub.createTokenFor("subject", "audience")


internal fun TestApplicationBuilder.mockApi(
    corsAllowedOrigins: String = "*.nav.no",
    corsAllowedSchemes: String = "https",
    corsAllowedHeaders: List<String> = emptyList(),
    meldekortService: MeldekortService = mockk(relaxed = true),
    oppfolgingService: OppfolgingService = mockk(relaxed = true),
    oppgaveService: OppgaveService = mockk(relaxed = true),
    beskjedMergerService: BeskjedMergerService = mockk(relaxed = true),
    innboksService: InnboksService = mockk(relaxed = true),
    sakerService: SakerService = mockk(relaxed = true),
    personaliaService: PersonaliaService = mockk(relaxed = true),
    unleashService: UnleashService = mockk(relaxed = true),
    digiSosService: DigiSosService = mockk(relaxed = true),
    doneProducer: DoneProducer = mockk(relaxed = true),
    httpClientIgnoreUnknownKeys: HttpClient = mockk(relaxed = true),

    ) {

    this.application {
        api(
            corsAllowedOrigins = corsAllowedOrigins,
            corsAllowedSchemes = corsAllowedSchemes,
            corsAllowedHeaders = corsAllowedHeaders,
            meldekortService = meldekortService,
            oppfolgingService = oppfolgingService,
            oppgaveService = oppgaveService,
            beskjedMergerService = beskjedMergerService,
            innboksService = innboksService,
            sakerService = sakerService,
            personaliaService = personaliaService,
            unleashService = unleashService,
            digiSosService = digiSosService,
            doneProducer = doneProducer,
            httpClient = httpClientIgnoreUnknownKeys,
            jwtAudience = "audience",
            jwkProvider = jwtStub.stubbedJwkProvider(),
            jwtIssuer = testIssuer
        )
    }
}

internal fun ApplicationTestBuilder.applicationHttpClient() =
    createClient {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            json(jsonConfig())
        }
        install(HttpTimeout)
    }

internal suspend fun ApplicationCall.respondRawJson(s: String) = respondBytes(
    contentType = ContentType.Application.Json,
    provider = { s.toByteArray() })

@Language("JSON")
internal fun rawEventHandlerVarsel(
    eventId: String = "12345",
    fodselsnummer: String = "5432176",
    grupperingsId: String = "gruppergrupp",
    førstBehandlet: String = "${ZonedDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS)}",
    produsent: String = "testprdusent",
    sikkerhetsnivå: Int = 4,
    sistOppdatert: String = "${ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS)}",
    tekst: String = "Teskt som er tekst som er tekst",
    link: String = "https://test.link.tadda",
    eksternVarslingSendt: Boolean = false,
    eksternVarslingKanaler: List<String> = emptyList(),
    aktiv: Boolean
): String =
    """ {
        "fodselsnummer" : "$fodselsnummer",
        "grupperingsId": "$grupperingsId",
        "eventId": "$eventId",
        "forstBehandlet": "$førstBehandlet",
        "produsent": "$produsent",
        "sikkerhetsnivaa": "$sikkerhetsnivå",
        "sistOppdatert": "$sistOppdatert",
        "tekst": "$tekst",
        "link": "$link",
        "aktiv": $aktiv,
        "eksternVarslingSendt": $eksternVarslingSendt,
        "eksternVarslingKanaler": $eksternVarslingKanaler 
         }
        """.trimIndent()

suspend fun HttpClient.authenticatedGet(urlString: String): HttpResponse = request {
    url(urlString)
    method = HttpMethod.Get
    header(HttpHeaders.Cookie, "selvbetjening-idtoken=$stubToken")
}

internal fun JsonObject.int(key: String): Int =
    this[key]?.jsonPrimitive?.int ?: throw IllegalArgumentException("Fant ikke integer med nøkkel $key")

internal fun JsonObject.bool(key: String): Boolean =
    this[key]?.jsonPrimitive?.boolean ?: throw IllegalArgumentException("Fant ikke boolean med nøkkel $key")

internal fun JsonObject.string(key: String): String =
    this[key]?.jsonPrimitive?.content ?: throw IllegalArgumentException("Fant ikke boolean med nøkkel $key")

internal fun JsonObject.localdate(key: String, datePattern: String? = null) =
    this.localdateOrNull(key,datePattern) ?: throw IllegalArgumentException("Fant ikke localdate med nøkkel $key")

internal fun JsonElement?.isNullObject(key: String): Boolean {
    return when {
        this == null -> true
        this.jsonObject[key] is JsonNull -> true
        else -> false
    }
}
internal fun JsonObject.localdateOrNull(key: String, datePattern: String? = null): LocalDate? =
    this[key]?.let { dateJsonObject ->
        if (dateJsonObject is JsonNull) {
            null
        } else {
            require(dateJsonObject.jsonPrimitive.isString)
            datePattern?.let {
                LocalDate.parse(dateJsonObject.jsonPrimitive.content, DateTimeFormatter.ofPattern(datePattern))
            } ?: LocalDate.parse(dateJsonObject.jsonPrimitive.content)
        }
    }
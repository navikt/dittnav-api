package no.nav.personbruker.dittnav.api.health

import io.ktor.application.ApplicationCall
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.personbruker.dittnav.api.config.Environment
import no.nav.personbruker.dittnav.api.config.HttpClientBuilder
import io.ktor.html.respondHtml
import kotlinx.html.*
import java.net.URL

suspend fun ApplicationCall.pingDependencies(environment: Environment) = coroutineScope {
    val client = HttpClientBuilder.build()

    val eventHandlerPingableURL = URL("${environment.dittNAVEventsURL}/isAlive")
    val legacyApiPingableURL = URL("${environment.dittNAVLegacyURL}/internal/isAlive")

    val eventHandlerSelftestStatus = async { getStatus(eventHandlerPingableURL, client) }
    val legacySelftestStatus = async { getStatus(legacyApiPingableURL, client) }


    val services =
        mapOf(
            "DITTNAV_LEGACY_API" to legacySelftestStatus.await(),
            "DITTNAV_EVENT_HANDLER" to eventHandlerSelftestStatus.await()
        )

    client.close()

    respondHtml {
        head {
            title { +"Selftest dittnav-api" }
        }
        body {
            table {
                thead {
                    tr { th { +"SELFTEST" } }
                }
                tbody {
                    services.map {
                        tr {
                            td { +it.key }
                            td { +it.value.pingedURL.toString() }
                            td {
                                style = if (it.value.status === Status.OK) "background: green" else "background: red;font-weight:bold"
                                +it.value.status.toString()
                            }
                            td { +it.value.statusMessage }
                        }
                    }
                }
            }
        }
    }
}

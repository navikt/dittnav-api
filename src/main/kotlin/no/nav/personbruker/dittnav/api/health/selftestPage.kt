package no.nav.personbruker.dittnav.api.health

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import kotlinx.html.*

suspend fun ApplicationCall.pingDependencies(dependencyPinger: DependencyPinger) = coroutineScope {
    val pingResults = dependencyPinger.pingAll()
    val serviceStatus = if (pingResults.values.any {result -> result.status == Status.ERROR }) Status.ERROR else Status.OK
    buildSelftestpage(serviceStatus, pingResults)
}

private suspend fun ApplicationCall.buildSelftestpage(
    serviceStatus: Status,
    services: MutableMap<String, SelftestStatus>
) {
    respondHtml(status =
        if (Status.ERROR == serviceStatus) {
            HttpStatusCode.ServiceUnavailable
        } else {
            HttpStatusCode.OK
        }
    )
    {
        head {
            title { +"Selftest dittnav-api" }
        }
        body {
            h1 {
                style = if (serviceStatus == Status.OK) "background: green" else "background: red;font-weight:bold"
                +"Service status: $serviceStatus"
            }
            table {
                thead {
                    tr { th { +"SELFTEST DITTNAV-API" } }
                }
                tbody {
                    services.map {
                        tr {
                            td { +it.key }
                            td { +it.value.pingedURL.toString() }
                            td {
                                style =
                                    if (it.value.status == Status.OK) "background: green" else "background: red;font-weight:bold"
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

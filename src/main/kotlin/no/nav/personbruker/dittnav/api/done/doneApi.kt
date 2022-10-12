package no.nav.personbruker.dittnav.api.done


import io.ktor.client.statement.HttpResponse
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.util.pipeline.*
import no.nav.personbruker.dittnav.api.config.authenticatedUser

fun Route.doneApi(doneProducer: DoneProducer) {

    post("/produce/done") {
            respondForParameterType<DoneDTO> { doneDto ->
                val response = doneProducer.postDoneEvents(doneDto, authenticatedUser)
                response
            }
    }
}

suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.respondForParameterType(handler: (T) -> HttpResponse) {
    val postParametersDto: T = call.receive()
    val httpResponse = handler.invoke(postParametersDto)
    call.respond(httpResponse.status)
}

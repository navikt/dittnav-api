package no.nav.personbruker.dittnav.api.done

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.client.statement.HttpResponse
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.util.pipeline.PipelineContext
import no.nav.personbruker.dittnav.api.config.innloggetBruker

fun Route.doneApi(doneProducer: DoneProducer) {

    post("/produce/done") {
        respondForParameterType<DoneDto> { doneDto ->
            val response = doneProducer.postDoneEvents(doneDto, innloggetBruker)
            response
        }
    }
}

suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.respondForParameterType(handler: (T) -> HttpResponse) {
    val postParametersDto: T = call.receive()
    val httpResponse = handler.invoke(postParametersDto)
    call.respond(httpResponse.status)
}

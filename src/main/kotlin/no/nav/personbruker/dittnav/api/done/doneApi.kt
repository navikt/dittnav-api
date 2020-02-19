package no.nav.personbruker.dittnav.api.done

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.util.pipeline.PipelineContext
import no.nav.personbruker.dittnav.api.common.innloggetBruker

fun Route.doneApi(doneProducer: DoneProducer) {

    post("/api/produce/done") {
        respondForParameterType<DoneDTO> { doneDto ->
            val response = doneProducer.postDoneEvents(doneDto, innloggetBruker)

            if (response.status == HttpStatusCode.OK) {
                "Done-event er sendt til handler for identen: ${innloggetBruker.getIdentFromToken()} sitt event med eventID: ${doneDto.eventId}."
            } else {
                "Error mot /handler/produce/done: ${response.status.value} ${response.status.description}"
            }
        }
    }
}

suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.respondForParameterType(handler: (T) -> String) {
    val postParametersDto: T = call.receive()
    val message = handler.invoke(postParametersDto)
    call.respondText(text = message, contentType = ContentType.Text.Plain)
}




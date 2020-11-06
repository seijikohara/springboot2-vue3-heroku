package com.example.demo.handler

import com.example.demo.config.ApplicationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class IndexHandler(
    @Autowired private val applicationProperties: ApplicationProperties
) {

    fun getIndexHtml(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_HTML)
            .bodyValue(applicationProperties.indexFile)
    }

}

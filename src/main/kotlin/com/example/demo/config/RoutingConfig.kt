package com.example.demo.config

import com.example.demo.handler.IndexHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class RoutingConfig(
    @Autowired private val indexHandler: IndexHandler,
) {

    @Bean
    fun apiRouter() = router {
        accept(MediaType.ALL).nest {
            GET("/", indexHandler::getIndexHtml)
        }
    }

}

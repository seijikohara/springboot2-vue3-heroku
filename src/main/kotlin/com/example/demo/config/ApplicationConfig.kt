package com.example.demo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

@Configuration
@EnableConfigurationProperties(ApplicationProperties::class)
class ApplicationConfig

@ConstructorBinding
@ConfigurationProperties(prefix = "application")
data class ApplicationProperties(
    val indexFile: Resource,
)

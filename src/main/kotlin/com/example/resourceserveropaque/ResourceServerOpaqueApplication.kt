package com.example.resourceserveropaque

import com.example.resourceserveropaque.security.token.configuration.TokenProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(value = [TokenProperties::class])
class ResourceServerOpaqueApplication

fun main(args: Array<String>) {
    runApplication<ResourceServerOpaqueApplication>(*args)
}

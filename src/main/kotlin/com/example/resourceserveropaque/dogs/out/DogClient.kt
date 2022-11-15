package com.example.resourceserveropaque.dogs.out

import com.example.resourceserveropaque.dogs.core.DogFact
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class DogClient(
    private val webClient: WebClient,
    @Value("\${dogService.baseUrl}") private val dogServiceBaseUrl: String
) {

    fun getRandomFact(): DogFact? {
        return webClient
            .get()
            .uri("$dogServiceBaseUrl/api/v1/resources/dogs?number=1")
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<List<DogFactResponse>>() {})
            .block()
            ?.body
            ?.first()
            ?.let { DogFact(it.fact) }
    }

    private data class DogFactResponse(val fact: String)
}

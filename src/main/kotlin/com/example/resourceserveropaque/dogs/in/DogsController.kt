package com.example.resourceserveropaque.dogs.`in`

import com.example.resourceserveropaque.dogs.core.GetRandomDogFact
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/dogs")
class DogsController(private val getRandomDogFact: GetRandomDogFact) {

    @GetMapping("/fact")
    fun getDogFact(): ResponseEntity<DogFactResponse> {
        return getRandomDogFact()
            ?.let { ResponseEntity.ok(DogFactResponse(it.fact)) }
            ?: ResponseEntity.internalServerError().build()
    }

    data class DogFactResponse(val fact: String)
}

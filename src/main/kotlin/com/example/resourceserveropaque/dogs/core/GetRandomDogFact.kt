package com.example.resourceserveropaque.dogs.core

import com.example.resourceserveropaque.dogs.out.DogClient
import org.springframework.stereotype.Service

@Service
class GetRandomDogFact(private val dogClient: DogClient) : () -> DogFact? {
    override fun invoke(): DogFact? = this.dogClient.getRandomFact()
}

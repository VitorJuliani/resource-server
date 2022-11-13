package com.example.resourceserveropaque.greeting

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/greeting")
class GreetingController {

    @GetMapping
    fun getGreetings(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello")
    }
}

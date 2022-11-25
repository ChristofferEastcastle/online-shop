package no.onlineshop.shippingservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shipping")
class ShippingController {

    @GetMapping
    fun fetchShippingDetails(): ResponseEntity<Any>{
        return ResponseEntity.ok().build()
    }
}
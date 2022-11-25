package no.onlineshop.shippingservice.controllers

import no.onlineshop.shippingservice.models.ShippingEntity
import no.onlineshop.shippingservice.services.ShippingHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shipping")
class ShippingController(@Autowired private val shippingHandler: ShippingHandler) {

    @GetMapping
    fun fetchShippingDetails(
        @RequestParam pageSize: Int,
        @RequestParam pageNumber: Int
    ): ResponseEntity<List<ShippingEntity>> {
        return ResponseEntity.ok(shippingHandler.fetchAllShipments(pageSize, pageNumber))
    }
}
package no.onlineshop.orderservice.controller

import no.onlineshop.orderservice.integration.RabbitSender
import no.onlineshop.orderservice.integration.ShipIntegrationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/order")
class OrderController(
    @Autowired private val rabbitSender: RabbitSender,
    @Autowired private val paymentIntegrationService: ShipIntegrationService
) {

    @GetMapping
    fun getOrderHello(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello from Order Service")
    }

    @PostMapping("/{message}")
    fun createOrderMessage(@PathVariable message: String) {
        rabbitSender.sendMessage(message)
    }

    @PostMapping("/callToPaymentService")
    fun getResponseFromShipService() {
        paymentIntegrationService.sendHttpCallToPaymentService("This is a message from the order service")
    }
}
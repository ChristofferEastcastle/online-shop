package no.onlineshop.paymentservice.controller

import no.onlineshop.paymentservice.integration.RabbitSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment")
class PaymentController(@Autowired private val rabbitSender: RabbitSender) {

    @GetMapping
    fun getPaymentHello(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello, from Payment Service")
    }

    @PostMapping("/{message}")
    fun createPaymentMessage(@PathVariable message: String) {
        rabbitSender.sendMessage(message)
    }

    @GetMapping("/http/{message}")
    fun responseToOrderService(@PathVariable message: String): ResponseEntity<String> {
        println(message)
        return ResponseEntity.ok("Hello from payment service, thank you for your request")
    }
}
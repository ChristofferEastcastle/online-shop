package no.onlineshop.paymentservice.controller

import no.onlineshop.paymentservice.models.PaymentCreateDto
import no.onlineshop.paymentservice.models.PaymentEntity
import no.onlineshop.paymentservice.services.PaymentHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payment")
class PaymentController(@Autowired private val paymentHandler: PaymentHandler) {

    @GetMapping
    fun fetchAllPayments(): ResponseEntity<List<PaymentEntity>> {
        return ResponseEntity.ok(paymentHandler.fetchAllPayments())
    }
    @PostMapping
    fun postPayment(@RequestBody paymentCreateDto: PaymentCreateDto): ResponseEntity<PaymentEntity> {
        return ResponseEntity.ok(paymentHandler.createPayment(paymentCreateDto))
    }
}
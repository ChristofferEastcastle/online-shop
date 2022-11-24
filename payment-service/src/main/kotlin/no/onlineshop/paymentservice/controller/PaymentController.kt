package no.onlineshop.paymentservice.controller

import no.onlineshop.paymentservice.integrationtest.PaymentHandler
import no.onlineshop.paymentservice.models.PaymentCreateDto
import no.onlineshop.paymentservice.models.PaymentEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@RestController
@RequestMapping("/api/payment")
class PaymentController(
    @Autowired private val paymentHandler: PaymentHandler,
    @Autowired private val restTemplate: RestTemplate,
    @Autowired private val env: Environment
) {

    @GetMapping
    fun fetchAllPayments(): ResponseEntity<List<PaymentEntity>> {
        return ResponseEntity.ok(paymentHandler.fetchAllPayments())
    }

    @PostMapping
    fun postPayment(@RequestBody paymentCreateDto: PaymentCreateDto): ResponseEntity<Any> {
        val url = env.getProperty("app.order-service.baseurl")
        restTemplate.getForEntity<Any>("${url}/api/order/${paymentCreateDto.orderId}/exists")
        return ResponseEntity.ok(paymentHandler.createPayment(paymentCreateDto))
    }
}
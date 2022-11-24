package no.onlineshop.orderservice.integration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class PaymentIntegrationService(@Autowired private val restTemplate: RestTemplate) {

    val paymentUrl = "http://payment-service/api/payment/ping"

    fun pingPaymentService(){

    }
}
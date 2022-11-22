package no.onlineshop.orderservice.integration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Service
class ShipIntegrationService(@Autowired private val restTemplate: RestTemplate) {

    //note that we are using the name of the service in eureka, ship-service, instead of the localhost:port
    val paymentUrl = "http://payment-service/api/payment/http"

    fun sendHttpCallToPaymentService(message: String) {
        val response: ResponseEntity<String> = restTemplate.getForEntity("$paymentUrl/$message", ResponseEntity::class)
        println("response from payment service: ${response.body}")
    }
}
package no.onlineshop.orderservice.integration

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PaymentIntegrationTest(@Autowired private val paymentIntegrationService: PaymentIntegrationService) {

    @Test
    fun pingTest(){
        val result = paymentIntegrationService.pingPaymentService()
        assertEquals(result, "payment service is up")
    }
}
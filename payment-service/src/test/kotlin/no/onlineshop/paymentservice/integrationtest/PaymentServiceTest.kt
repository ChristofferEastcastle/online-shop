package no.onlineshop.paymentservice.integrationtest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.onlineshop.paymentservice.models.PaymentCreateDto
import no.onlineshop.paymentservice.models.PaymentEntity
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class PaymentServiceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val paymentEndpoint = "/api/payment"

    @Test
    fun createPayment() {
        val payment = PaymentCreateDto(1, 1)
        val result = PaymentEntity(1, 1, 1)
        val objectMapper = jacksonObjectMapper()
        mockMvc.post(paymentEndpoint) {
            content = objectMapper.writeValueAsString(payment)
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect { status { is2xxSuccessful() } }
            .andExpect { content { string(objectMapper.writeValueAsString(result)) } }
    }

    @Test
    fun fetchAllPayments() {
        val expected = listOf(PaymentEntity(1, 1, 1))
        mockMvc.get(paymentEndpoint)
            .andExpect { status { is2xxSuccessful() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON)} }
    }

}
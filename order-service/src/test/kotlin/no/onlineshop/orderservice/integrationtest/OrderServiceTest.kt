package no.onlineshop.orderservice.integrationtest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.onlineshop.orderservice.models.OrderEntity
import no.onlineshop.orderservice.models.OrderPostDto
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
class OrderServiceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun addOrderTest() {
        val order = OrderPostDto("phone", 1, 1)
        val expected = OrderEntity(1, "phone", 1)
        mockMvc.post("/api/order") {
            content = jacksonObjectMapper().writeValueAsString(order)
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect { status { is2xxSuccessful() } }
            .andExpect { content { string(jacksonObjectMapper().writeValueAsString(expected)) } }

    }

    @Test
    fun fetchOrders() {
        val order = OrderPostDto("phone", 1, 1)
        mockMvc.post("/api/order") {
            content = jacksonObjectMapper().writeValueAsString(order)
            contentType = MediaType.APPLICATION_JSON
        }
        mockMvc.get("/api/order")
            .andExpect { content { status { is2xxSuccessful() } } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }
}
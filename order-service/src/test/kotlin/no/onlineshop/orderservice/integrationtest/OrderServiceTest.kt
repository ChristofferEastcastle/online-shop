package no.onlineshop.orderservice.integrationtest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import no.onlineshop.orderservice.models.OrderEntity
import no.onlineshop.orderservice.models.OrderPostDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextClosedEvent
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.client.RestTemplate


@SpringBootTest
@ContextConfiguration(initializers = [WireMockContextInitializer::class])
@WireMockTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc


    @Test
    fun addOrderTest() {
        val order = OrderPostDto("phone", 1)
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
        val order = OrderPostDto("phone", 1)

        mockMvc.post("/api/order") {
            content = jacksonObjectMapper().writeValueAsString(order)
            contentType = MediaType.APPLICATION_JSON
        }
        mockMvc.get("/api/order") {
            param("pageSize", "5")
            param("pageNumber", "0")
        }
            .andExpect { content { status { is2xxSuccessful() } } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }
}

class WireMockContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val wmServer = WireMockServer(WireMockConfiguration().dynamicPort())
        wmServer.start()

        applicationContext.beanFactory.registerSingleton("wireMock", wmServer)
        applicationContext.addApplicationListener {
            if (it is ContextClosedEvent) {
                wmServer.stop()
            }
        }
        System.setProperty("spring.rabbitmq.host", "localhost")
        val port = wmServer.port()
        System.setProperty("spring.rabbitmq.port", port.toString())
        WireMock.configureFor("localhost", port)
    }

    companion object {
        @Configuration
        class TestConfig {
            @Bean
            @Profile("test")
            fun restTemplate(): RestTemplate = RestTemplate()
        }
    }
}
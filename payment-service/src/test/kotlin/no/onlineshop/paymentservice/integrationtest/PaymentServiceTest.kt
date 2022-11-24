package no.onlineshop.paymentservice.integrationtest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import no.onlineshop.paymentservice.models.PaymentCreateDto
import no.onlineshop.paymentservice.models.PaymentEntity
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextClosedEvent
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.client.RestTemplate


@SpringBootTest
@ContextConfiguration(initializers = [WireMockContextInitializer::class])
@WireMockTest
@AutoConfigureMockMvc
class PaymentServiceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var env: Environment

    private val paymentEndpoint = "/api/payment"

    @Autowired
    private lateinit var wiremockServer: WireMockServer


    @Test
    fun createPayment() {
        val payment = PaymentCreateDto(1, 1)
        val objectMapper = jacksonObjectMapper()
        wiremockServer.stubFor(
            get(urlEqualTo("/api/order/${payment.orderId}/exists"))
                .willReturn(
                    aResponse()
                        .withStatus(404)
                )
        )

        mockMvc.post(urlTemplate = "http://localhost/$paymentEndpoint") {
            content = objectMapper.writeValueAsString(payment)
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isBadRequest() } }
            .andExpect { content { string("You can not pay for this order, it does not exist!") } }
    }

    @Test
    fun fetchAllPayments() {
        mockMvc.get(paymentEndpoint)
            .andExpect { status { is2xxSuccessful() } }
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
        System.setProperty("app.order-service.baseurl", "http://localhost:${wmServer.port()}")
        configureFor("localhost", wmServer.port())
        configureFor("order-service", wmServer.port())
        TestPropertyValues
            .of("app.order-service.url=http://localhost:${wmServer.port()}")
            .applyTo(applicationContext)
    }

    companion object{
        @Configuration
        class TestConfig{
            @Bean
            fun restTemplate(): RestTemplate = RestTemplate()
        }
    }
}
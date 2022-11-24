package no.onlineshop.orderservice.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.junit.jupiter.Container
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.time.Duration

@TestConfiguration
class Test {
    @Bean
    fun mock(): RabbitReceiver = mockk<RabbitReceiver>()
}

//@SpringBootTest
@AutoConfigureMockMvc
class RabbitSenderTest(
    @Autowired private val rabbitSender: RabbitSender,
) {

    @Autowired
    private lateinit var mockMvc: MockMvc

    companion object {

        /*
            workaround to current Kotlin (and other JVM languages) limitation
            see https://github.com/testcontainers/testcontainers-java/issues/318
         */
        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        /*
            This will start a RabbitMQ server using Docker.
            This is "similar" to start the following from command-line:
            docker run -p 5672:5672 rabbitmq:3
            However, here, although the port is exposed, it is mapped to a
            random, free one.
         */
        val newNetwork: Network = Network.newNetwork()
        @Container
        @JvmField
        val rabbitMQ = KGenericContainer("rabbitmq:3.11.3-management-alpine")
            .withExposedPorts(5672)
            .withNetwork(newNetwork)

        @Container
        @JvmField
        val discoveryService = KGenericContainer("discovery-service")
            .withExposedPorts(8761)
            .withNetwork(newNetwork)

        @Container
        @JvmField
        val gatewayService = KGenericContainer("gateway-service")
            .withExposedPorts(8080)
            .withNetwork(newNetwork)


        @Container
        @JvmField
        val paymentService: KGenericContainer = KGenericContainer("payment-service")
            .withExposedPorts(8091)
            .withStartupTimeout(Duration.ofSeconds(10))
            .withNetwork(newNetwork)

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            discoveryService.start()
            gatewayService.start()
            rabbitMQ.start()
            paymentService.addEnv("spring_profiles_active", "test")
            paymentService.addEnv("RABBIT_HOST", rabbitMQ.containerIpAddress)
            paymentService.addEnv("RABBIT_PORT", rabbitMQ.getMappedPort(5672).toString())
            paymentService.addEnv("EUREKA_HOST", discoveryService.containerIpAddress)
            paymentService.addEnv("EUREKA_PORT", discoveryService.getMappedPort(8761).toString())
            paymentService.start()
            Thread.sleep(10000)
        }
    }

    //@Test
    fun sendMessageTest() {
        val printStream = System.out
        val stream = ByteArrayOutputStream()
        System.setOut(PrintStream(stream))
        rabbitSender.sendMessage("test")
        val mappedPort = paymentService.getMappedPort(8091)
        val res = URL("http://localhost:$mappedPort/api/payment").readText()
        System.setOut(printStream)
        paymentService.followOutput {
            println(it.utf8String)
        }

        Thread.sleep(5000)

        println(stream.toString())
        println(res)
//        Assertions.assertEquals(jacksonObjectMapper().writeValueAsString(listOf<String>()), res)
//        val payment = """
//            {"orderId": 1, "userId": 1}
//        """.trimIndent()
//        val request = HttpRequest
//            .newBuilder(URI("http://localhost:$mappedPort/api/payment"))
//            .header("Content-Type", "application/json")
//            .POST(BodyPublishers.ofString(payment))
//            .build()
//        val client = HttpClient.newHttpClient()
//        val out = paymentService.followOutput {
//            println(it.utf8String)
//        }
//        val res = client.send(request, HttpResponse.BodyHandlers.ofString())
//        println(res.body())


    }
}
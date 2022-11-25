//package no.onlineshop.orderservice.integration
//
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import no.onlineshop.orderservice.services.Action
//import no.onlineshop.orderservice.services.Message
//import org.junit.jupiter.api.BeforeAll
//import org.junit.jupiter.api.Test
//import org.springframework.amqp.core.AmqpAdmin
//import org.springframework.amqp.core.Queue
//import org.springframework.amqp.rabbit.core.RabbitAdmin
//import org.springframework.amqp.rabbit.core.RabbitTemplate
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Profile
//import org.springframework.core.env.Environment
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.web.client.RestTemplate
//import org.testcontainers.containers.BindMode
//import org.testcontainers.containers.GenericContainer
//import org.testcontainers.containers.Network
//import org.testcontainers.junit.jupiter.Container
//import java.io.ByteArrayOutputStream
//import java.io.PrintStream
//import java.net.URL
//import java.nio.file.Files
//import java.time.Duration
//import kotlin.io.path.Path
//
////@SpringBootTest
////@ActiveProfiles("test")
////@AutoConfigureMockMvc
//class RabbitSenderTest(
//    @Autowired private val rabbitSender: RabbitSender,
//    @Autowired private val env: Environment,
//    @Autowired private val admin: AmqpAdmin
//) {
//
//    //@Autowired
//    private lateinit var mockMvc: MockMvc
//
//    companion object {
//
//        @Bean
//        @Profile("test")
//        fun amqpAdmin(): AmqpAdmin{
//            val template = RabbitTemplate()
//            template.setDefaultReceiveQueue("shipping_queue")
//            return RabbitAdmin(template)
//        }
//
//        @Bean
//        @Profile("test")
//        fun restTemplate(): RestTemplate = RestTemplate()
//
//        /*
//            workaround to current Kotlin (and other JVM languages) limitation
//            see https://github.com/testcontainers/testcontainers-java/issues/318
//         */
//        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)
//
//        /*
//            This will start a RabbitMQ server using Docker.
//            This is "similar" to start the following from command-line:
//            docker run -p 5672:5672 rabbitmq:3
//            However, here, although the port is exposed, it is mapped to a
//            random, free one.
//         */
//        val newNetwork: Network = Network.newNetwork()
//
//        @Container
//        @JvmField
//        val rabbitMQ = KGenericContainer("rabbitmq:3.11.3-management-alpine")
//            .withExposedPorts(5672)
//            .withNetwork(newNetwork)
//
//        @Container
//        @JvmField
//        val discoveryService = KGenericContainer("discovery-service")
//            .withExposedPorts(8761)
//            .withNetwork(newNetwork)
//
//        @Container
//        @JvmField
//        val gatewayService = KGenericContainer("gateway-service")
//            .withExposedPorts(8080)
//            .withNetwork(newNetwork)
//
//
//        @Container
//        @JvmField
//        val shippingService: KGenericContainer = KGenericContainer("shipping-service")
//            .withExposedPorts(8092)
//            .withStartupTimeout(Duration.ofSeconds(10))
//            .withNetwork(newNetwork)
//
//        @BeforeAll
//        @JvmStatic
//        internal fun beforeAll() {
//            discoveryService.start()
////            gatewayService.start()
//            rabbitMQ.addFileSystemBind("rabbitmq-config/rabbitmq.config", "/etc/rabbitmq/rabbitmq.config", BindMode.READ_ONLY)
//            rabbitMQ.addFileSystemBind("rabbitmq-config/definitions.json", "/etc/rabbitmq/definitions.json", BindMode.READ_ONLY)
//            println(Files.exists(Path("rabbitmq-config/rabbitmq.config")))
//            rabbitMQ.start()
//            shippingService.addEnv("spring_profiles_active", "test")
//            shippingService.addEnv("RABBIT_HOST", rabbitMQ.containerIpAddress)
//            shippingService.addEnv("RABBIT_PORT", rabbitMQ.getMappedPort(5672).toString())
//            shippingService.addEnv("EUREKA_HOST", discoveryService.containerIpAddress)
//            shippingService.addEnv("EUREKA_PORT", discoveryService.getMappedPort(8761).toString())
//            shippingService.start()
//            System.setProperty("spring.rabbitmq.host", rabbitMQ.containerIpAddress)
//            System.setProperty("spring.rabbitmq.port", rabbitMQ.getMappedPort(5672).toString())
//
//        }
//
//    }
////
////    @Test
////    fun sendMessageTest() {
////        admin.declareQueue(Queue("shipping_queue"))
////        admin.declareQueue(Queue("order_queue"))
////        val printStream = System.out
////        val stream = ByteArrayOutputStream()
////        System.setOut(PrintStream(stream))
////        val message = Message(1, Action.SHIPPED, "Order with id 1 has been shipped!")
////        rabbitSender.sendMessage(jacksonObjectMapper().writeValueAsString(message), "shipping_queue")
////        val mappedPort = shippingService.getMappedPort(8092)
////        val res = URL("http://localhost:$mappedPort/api/shipping?pageSize=5&pageNumber=0").readText()
////        System.setOut(printStream)
////        shippingService.followOutput {
////            println(it.utf8String)
////        }
////        println(stream.toString())
////        println(res)
////    }
//}
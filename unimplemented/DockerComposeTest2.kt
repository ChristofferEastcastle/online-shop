package no.onlineshop.orderservice.integration

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import java.io.File
import java.net.URL

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class DockerComposeTest {

    companion object {
        @delegate:Container
        private val instance: KDockerComposeContainer by lazy { defineDockerCompose()}

        class KDockerComposeContainer(file: File) : DockerComposeContainer<KDockerComposeContainer>(file)

        private fun defineDockerCompose() = KDockerComposeContainer(File("../docker-compose-test.yml"))
            .withLocalCompose(true)
            .withExposedService("database", 5432)
            .withExposedService("order-service", 8090)
            .waitingFor("order-service", Wait.forHealthcheck())

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            instance.waitingFor("order-service", Wait.forHttp("/api/payment"))
            instance.withExposedService("order-service", 8090)
            instance.start()
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            instance.stop()
        }
    }

//    @Test
//    fun test() {
//        instance.start()
//        instance.waitingFor("order-service", Wait.defaultWaitStrategy())
//        val host = instance.getServiceHost("order-service", 8090)
//        val port = instance.getServicePort("order-service", 8090)
//
//        println(URL("$host:$port/api/order").readText())
//    }
}
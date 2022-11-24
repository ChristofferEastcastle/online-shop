package no.onlineshop.paymentservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@SpringBootApplication
@EnableEurekaClient
class PaymentServiceApplication {

    @Bean
    @LoadBalanced
    fun restTemplates(): RestTemplate = RestTemplate()

}

fun main(args: Array<String>) {
    runApplication<PaymentServiceApplication>(*args)
}

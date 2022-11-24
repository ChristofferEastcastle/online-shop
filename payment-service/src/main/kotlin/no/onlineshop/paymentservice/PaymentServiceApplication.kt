package no.onlineshop.paymentservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestTemplate

@SpringBootApplication
@EnableEurekaClient
class PaymentServiceApplication {

    @Bean
    @LoadBalanced
    @Profile("default")
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    @Profile("local")
    fun restTemplateForLocal(): RestTemplate = RestTemplate()


}

fun main(args: Array<String>) {
    runApplication<PaymentServiceApplication>(*args)
}

package no.onlineshop.orderservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class OrderServiceApplication {

    @Bean
    @LoadBalanced
    @Profile("default")
    fun restTemplate(): RestTemplate? {
        return RestTemplate()
    }

    @Bean
    @Profile("local")
    fun restTemplateForLocal(): RestTemplate = RestTemplate()
}

fun main(args: Array<String>) {
    runApplication<OrderServiceApplication>(*args)
}
package no.onlineshop.paymentservice

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestTemplate
import java.time.Duration

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

    @Bean
    fun globalCustomConfiguration(): Customizer<Resilience4JCircuitBreakerFactory> {

        val circuitBreakerConfig = CircuitBreakerConfig.custom()
            // how many failures (in %) before activating the CB?
            .failureRateThreshold(51f)
            // the number of most recent calls on which failure rate is calculated
            .slidingWindowSize(2)
            //for how long should the CB stop requests once on?
            .waitDurationInOpenState(Duration.ofMillis(5000))
            .build()
        val timeLimiterConfig = TimeLimiterConfig.custom()
            // how long to wait before giving up a request?
            .timeoutDuration(Duration.ofMillis(500))
            .build()

        return Customizer<Resilience4JCircuitBreakerFactory> { factory ->
            factory.configureDefault { id ->
                Resilience4JConfigBuilder(id)
                    .timeLimiterConfig(timeLimiterConfig)
                    .circuitBreakerConfig(circuitBreakerConfig)
                    .build()
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<PaymentServiceApplication>(*args)
}

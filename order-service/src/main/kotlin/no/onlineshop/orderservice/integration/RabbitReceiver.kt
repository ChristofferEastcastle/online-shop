package no.onlineshop.orderservice.integration

import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
@RabbitListener(queues = ["payment_queue"])
class RabbitReceiver {
    @RabbitHandler
    fun receive(message: String) {
        println("Received: $message from payment queue")
    }
}
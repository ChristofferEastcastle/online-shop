package no.onlineshop.paymentservice.integration

import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
@RabbitListener(queues = ["order_queue"])
class RabbitReceiver {
    @RabbitHandler
    fun receive(message: String) {
        println("Received: $message from order queue")
    }
}
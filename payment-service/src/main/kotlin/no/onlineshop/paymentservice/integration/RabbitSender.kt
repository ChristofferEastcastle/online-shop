package no.onlineshop.paymentservice.integration

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RabbitSender(@Autowired private val rabbitTemplate: RabbitTemplate) {

    fun sendMessage(message: String) {
        rabbitTemplate.convertAndSend("payment_queue", message)
    }

}
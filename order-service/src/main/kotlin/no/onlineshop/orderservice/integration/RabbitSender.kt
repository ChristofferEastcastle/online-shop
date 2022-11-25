package no.onlineshop.orderservice.integration

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RabbitSender(@Autowired private val rabbitTemplate: RabbitTemplate) {

    fun sendMessage(message: String, queue: String) {
        println("sending message: $message")
        try {
            rabbitTemplate.convertAndSend(queue, message)
        } catch (ex: RuntimeException){
            println(ex.message)
        }
    }
}
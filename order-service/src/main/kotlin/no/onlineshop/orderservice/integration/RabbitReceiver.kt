package no.onlineshop.orderservice.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.onlineshop.orderservice.services.Action
import no.onlineshop.orderservice.services.Message
import no.onlineshop.orderservice.services.OrderHandler
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@RabbitListener(queues = ["payment_queue", "shipping_queue"])
class RabbitReceiver(@Autowired private val orderHandler: OrderHandler) {
    @RabbitHandler
    fun receive(message: String) {
        println("Received: $message from queue")
        val msg = jacksonObjectMapper().readValue(message, Message::class.java)
        if (msg.action == Action.SHIPPED){
            orderHandler.updateOrderToShipped(msg.orderId)
        }
    }
}
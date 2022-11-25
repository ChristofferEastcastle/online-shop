package no.onlineshop.shippingservice.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.onlineshop.shippingservice.models.Action
import no.onlineshop.shippingservice.models.Message
import no.onlineshop.shippingservice.services.ShippingHandler
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@RabbitListener(queues = ["order_queue"])
class RabbitReceiver(@Autowired private val shippingHandler: ShippingHandler) {
    @RabbitHandler
    fun receive(message: String) {
        println("Received: $message from order queue")
        val msg = jacksonObjectMapper().readValue(message, Message::class.java)
        if (msg.action == Action.PAYMENT){
            shippingHandler.addShipping(msg.orderId)
        }
    }
}
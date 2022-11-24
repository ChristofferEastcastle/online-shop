package no.onlineshop.orderservice.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.onlineshop.orderservice.exceptions.OrderException
import no.onlineshop.orderservice.integration.RabbitSender
import no.onlineshop.orderservice.models.OrderEntity
import no.onlineshop.orderservice.models.OrderPostDto
import no.onlineshop.orderservice.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderHandler(
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val rabbitSender: RabbitSender
) {
    fun fetchAllOrders(): List<OrderEntity> {
        return orderRepository.findAll()
    }

    fun addNewOrder(newOrder: OrderPostDto): OrderEntity {
        val orderEntity = OrderEntity(
            type = newOrder.type,
            qty = newOrder.qty
        )
        val savedOrder = orderRepository.save(orderEntity)
        savedOrder.id?.let {
            val message = Message(newOrder.userId, it, Action.WAIT_PAYMENT, "new order with id ${savedOrder.id} placed. Waiting for payment..")
            rabbitSender.sendMessage(jacksonObjectMapper().writeValueAsString(message))
            return savedOrder
        }
        throw OrderException("Could not store order")
    }
}

data class Message(
    val userId: Long,
    val orderId: Long,
    val action: Action,
    val message: String,
)

enum class Action{
    WAIT_PAYMENT
}
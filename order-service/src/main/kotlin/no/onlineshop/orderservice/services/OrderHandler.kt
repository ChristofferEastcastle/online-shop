package no.onlineshop.orderservice.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.onlineshop.orderservice.exceptions.OrderException
import no.onlineshop.orderservice.exceptions.OrderUpdateException
import no.onlineshop.orderservice.integration.RabbitSender
import no.onlineshop.orderservice.models.OrderEntity
import no.onlineshop.orderservice.models.OrderPostDto
import no.onlineshop.orderservice.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class OrderHandler(
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val rabbitSender: RabbitSender
) {
    fun fetchAllOrders(pageSize: Int, pageNumber: Int): List<OrderEntity> {
        return orderRepository.findAll(Pageable.ofSize(pageSize).withPage(pageNumber)).toList()
    }

    fun addNewOrder(newOrder: OrderPostDto): OrderEntity {
        val orderEntity = OrderEntity(
            type = newOrder.type,
            qty = newOrder.qty
        )
        val savedOrder = orderRepository.save(orderEntity)
        savedOrder.id?.let {
            val message = Message(it, Action.WAIT_PAYMENT, "new order with id ${savedOrder.id} placed. Waiting for payment..")
            rabbitSender.sendMessage(jacksonObjectMapper().writeValueAsString(message), "order_queue")
            return savedOrder
        }
        throw OrderException("Could not store order")
    }

    fun orderExists(id: Long): Boolean {
        return orderRepository.existsById(id)
    }

    fun updateOrderToPaid(id: Long): OrderEntity {
        val optional = orderRepository.findById(id)
        if (optional.isPresent) {
            val orderEntity = optional.get()
            val updated = OrderEntity(
                id = orderEntity.id,
                type = orderEntity.type,
                qty = orderEntity.qty,
                isPaid = true,
                isShipped = orderEntity.isShipped
            )
            return orderRepository.save(updated)
        }
        throw OrderUpdateException("Could not update, the order does not exist!")
    }

    fun updateOrderToShipped(id: Long) {
        val optional = orderRepository.findById(id)
        if (optional.isPresent) {
            val orderEntity = optional.get()
            val updated = OrderEntity(
                id = orderEntity.id,
                type = orderEntity.type,
                qty = orderEntity.qty,
                isPaid = orderEntity.isPaid,
                isShipped = true
            )
            orderRepository.save(updated)
        }
    }

    fun tellShippingService(id: Long) {
        val message = Message(id, Action.PAYMENT, "Order with id $id has been paid. Time to ship it!")
        rabbitSender.sendMessage(jacksonObjectMapper().writeValueAsString(message), "order_queue")
    }
}

data class Message(
    val orderId: Long,
    val action: Action,
    val message: String,
)

enum class Action{
    WAIT_PAYMENT,
    PAYMENT,
    SHIPPED
}
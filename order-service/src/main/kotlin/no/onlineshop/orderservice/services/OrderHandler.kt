package no.onlineshop.orderservice.services

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

    fun addNewOrder(newOrder: OrderPostDto) {
        val orderEntity = OrderEntity().apply {
            type = newOrder.type
            qty = newOrder.qty
        }
        val savedOrder = orderRepository.save(orderEntity)
        rabbitSender.sendMessage("new order with id ${savedOrder.id} placed. Waiting for payment..")
    }
}
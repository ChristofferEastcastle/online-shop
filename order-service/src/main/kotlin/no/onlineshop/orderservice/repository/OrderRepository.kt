package no.onlineshop.orderservice.repository

import no.onlineshop.orderservice.models.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: JpaRepository<OrderEntity, Long> {
    //fun saveOrder(orderEntity: OrderEntity): OrderEntity
}
package no.onlineshop.shippingservice.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.onlineshop.shippingservice.integration.RabbitSender
import no.onlineshop.shippingservice.models.Action
import no.onlineshop.shippingservice.models.Message
import no.onlineshop.shippingservice.models.ShippingEntity
import no.onlineshop.shippingservice.repository.ShippingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ShippingHandler(
    @Autowired private val shippingRepository: ShippingRepository,
    @Autowired private val rabbitSender: RabbitSender
) {

    fun fetchAllShipments(pageSize: Int, pageNumber: Int): List<ShippingEntity> {
        return shippingRepository.findAll(Pageable.ofSize(pageSize).withPage(pageNumber)).toList()
    }

    fun addShipping(orderId: Long): ShippingEntity {
        val shippingEntity = shippingRepository.save(ShippingEntity(orderId = orderId))
        val message =
            Message(shippingEntity.orderId, Action.SHIPPED, "Order with id ${shippingEntity.orderId} has been shipped!")
        rabbitSender.sendMessage(jacksonObjectMapper().writeValueAsString(message))
        return shippingEntity
    }
}
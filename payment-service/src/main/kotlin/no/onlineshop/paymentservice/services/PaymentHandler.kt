package no.onlineshop.paymentservice.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.onlineshop.paymentservice.integration.RabbitSender
import no.onlineshop.paymentservice.models.Action
import no.onlineshop.paymentservice.models.Message
import no.onlineshop.paymentservice.models.PaymentCreateDto
import no.onlineshop.paymentservice.models.PaymentEntity
import no.onlineshop.paymentservice.repository.PaymentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PaymentHandler(@Autowired private val rabbitSender: RabbitSender, @Autowired private val paymentRepository: PaymentRepository) {

    fun fetchAllPayments(pageSize: Int, pageNumber: Int): List<PaymentEntity> {
        return paymentRepository.findAll(Pageable.ofSize(pageSize).withPage(pageNumber)).toList()
    }

    fun createPayment(newPayment: PaymentCreateDto): PaymentEntity {
        val optional = paymentRepository.findByOrderId(newPayment.orderId)
        if (optional.isPresent) return optional.get()

        val savedPayment = paymentRepository.save(PaymentEntity(
           orderId = newPayment.orderId))
        val message = Message(newPayment.orderId, Action.PAYMENT, "User has paid for order with id ${newPayment.orderId}")
        rabbitSender.sendMessage(jacksonObjectMapper().writeValueAsString(message))
        return savedPayment
    }
}
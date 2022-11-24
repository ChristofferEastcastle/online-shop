package no.onlineshop.paymentservice.integrationtest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.onlineshop.paymentservice.models.Action
import no.onlineshop.paymentservice.models.Message
import no.onlineshop.paymentservice.integration.RabbitSender
import no.onlineshop.paymentservice.models.PaymentCreateDto
import no.onlineshop.paymentservice.models.PaymentEntity
import no.onlineshop.paymentservice.repository.PaymentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PaymentHandler(@Autowired private val rabbitSender: RabbitSender, @Autowired private val paymentRepository: PaymentRepository) {

    fun fetchAllPayments(): List<PaymentEntity> {
        return paymentRepository.findAll()
    }

    fun createPayment(newPayment: PaymentCreateDto): PaymentEntity {
        val savedPayment = paymentRepository.save(PaymentEntity(
           orderId = newPayment.orderId,
           userId = newPayment.userId
        ))//.apply { orderId = newPayment.orderId})//; userId = newPayment.userId })
        val message = Message(newPayment.userId, newPayment.orderId, Action.PAYMENT, "User with id ${newPayment.userId} has paid for order with id ${newPayment.orderId}")
        rabbitSender.sendMessage(jacksonObjectMapper().writeValueAsString(message))
        return savedPayment
    }
}
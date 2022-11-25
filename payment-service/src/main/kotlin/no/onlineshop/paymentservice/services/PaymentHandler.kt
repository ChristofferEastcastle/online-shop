package no.onlineshop.paymentservice.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.onlineshop.paymentservice.integration.RabbitSender
import no.onlineshop.paymentservice.models.Action
import no.onlineshop.paymentservice.models.Message
import no.onlineshop.paymentservice.models.PaymentCreateDto
import no.onlineshop.paymentservice.models.PaymentEntity
import no.onlineshop.paymentservice.repository.PaymentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity

@Service
class PaymentHandler(
    @Autowired private val rabbitSender: RabbitSender,
    @Autowired private val restTemplate: RestTemplate,
    @Autowired private val paymentRepository: PaymentRepository,
    @Autowired private val env: Environment
) {

    fun fetchAllPayments(pageSize: Int, pageNumber: Int): List<PaymentEntity> {
        return paymentRepository.findAll(Pageable.ofSize(pageSize).withPage(pageNumber)).toList()
    }

    fun createPayment(newPayment: PaymentCreateDto): PaymentEntity {
        val optional = paymentRepository.findByOrderId(newPayment.orderId)
        if (optional.isPresent) return optional.get()

        val savedPayment = paymentRepository.save(
            PaymentEntity(
                orderId = newPayment.orderId
            )
        )
        // We send a message that the user has paid for the order. But exam assignment wants to have synchronous update of order when then payment is done.
        // Therefore, we use restTemplate to tell order-service that the order has been paid
        val message =
            Message(newPayment.orderId, Action.PAYMENT, "User has paid for order with id ${newPayment.orderId}")
        rabbitSender.sendMessage(jacksonObjectMapper().writeValueAsString(message))
        restTemplate.postForEntity<Any>("${env.getProperty("app.order-service.baseurl")}/api/order/${savedPayment.orderId}/paid")
        return savedPayment
    }
}
package no.onlineshop.paymentservice.repository

import no.onlineshop.paymentservice.models.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository: JpaRepository<PaymentEntity, Long> {
    override fun findAll(): List<PaymentEntity>
}
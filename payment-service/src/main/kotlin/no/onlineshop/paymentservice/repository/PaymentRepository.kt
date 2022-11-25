package no.onlineshop.paymentservice.repository

import no.onlineshop.paymentservice.models.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PaymentRepository: JpaRepository<PaymentEntity, Long> {
    fun findByOrderId(id: Long): Optional<PaymentEntity>
}
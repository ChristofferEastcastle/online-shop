package no.onlineshop.shippingservice.repository

import no.onlineshop.shippingservice.models.ShippingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShippingRepository: JpaRepository<ShippingEntity, Long> {
}
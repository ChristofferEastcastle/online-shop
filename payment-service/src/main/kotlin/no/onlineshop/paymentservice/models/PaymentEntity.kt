package no.onlineshop.paymentservice.models

import org.jetbrains.annotations.NotNull
import javax.persistence.*

@Entity
@Table(name = "payments")
class PaymentEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    var id: Long? = null,
    @Column(name = "order_id")
    @NotNull
    var orderId: Long
)
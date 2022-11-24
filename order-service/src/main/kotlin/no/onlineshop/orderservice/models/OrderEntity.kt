package no.onlineshop.orderservice.models

import javax.persistence.*

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val type: String,
    val qty: Int,
    @Column(name = "is_paid")
    val isPaid: Boolean = false,
    @Column(name = "is_shipped")
    val isShipped: Boolean = false
)
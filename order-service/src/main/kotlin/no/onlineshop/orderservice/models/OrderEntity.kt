package no.onlineshop.orderservice.models

import javax.persistence.*

@Entity
@Table(name = "orders")
class OrderEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var type: String? = null
    var qty: Int? = null
    @Column(name = "is_paid")
    val isPaid: Boolean = false
    @Column(name = "is_shipped")
    val isShipped: Boolean = false
}
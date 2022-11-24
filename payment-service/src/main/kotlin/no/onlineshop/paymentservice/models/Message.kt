package no.onlineshop.paymentservice.models

data class Message(
    val userId: Long,
    val orderId: Long,
    val action: Action,
    val message: String,
)

enum class Action{
    WAIT_PAYMENT,
    PAYMENT
}
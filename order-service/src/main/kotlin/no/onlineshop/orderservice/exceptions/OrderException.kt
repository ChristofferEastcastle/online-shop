package no.onlineshop.orderservice.exceptions

class OrderException(message: String): RuntimeException(message)
class OrderUpdateException(message: String): RuntimeException(message)
package no.onlineshop.orderservice.controller

import no.onlineshop.orderservice.models.OrderEntity
import no.onlineshop.orderservice.models.OrderPostDto
import no.onlineshop.orderservice.services.OrderHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/order")
class OrderController(
    @Autowired private val orderHandler: OrderHandler
) {

    @GetMapping
    fun fetchAllOrders(): ResponseEntity<List<OrderEntity>>{
        return ResponseEntity.ok(orderHandler.fetchAllOrders())
    }

    @PostMapping
    fun postNewOrder(@RequestBody newOrder: OrderPostDto): ResponseEntity<OrderEntity>{
        return ResponseEntity.ok(orderHandler.addNewOrder(newOrder))
    }
}
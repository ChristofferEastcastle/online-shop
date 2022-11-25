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
    fun fetchAllOrders(@RequestParam pageSize: Int, pageNumber: Int): ResponseEntity<List<OrderEntity>> {
        return ResponseEntity.ok(orderHandler.fetchAllOrders(pageSize, pageNumber))
    }

    @PostMapping
    fun postNewOrder(@RequestBody newOrder: OrderPostDto): ResponseEntity<OrderEntity> {
        return ResponseEntity.ok(orderHandler.addNewOrder(newOrder))
    }

    @GetMapping("{id}/exists")
    fun orderExists(@PathVariable id: Long): ResponseEntity<Any> {
        return when (orderHandler.orderExists(id)) {
            true -> ResponseEntity.ok().build()
            false -> ResponseEntity.notFound().build()
        }
    }

    @PostMapping("{id}/paid")
    fun updateOrderToPaid(@PathVariable id: Long): ResponseEntity<Any> {
        val updated = orderHandler.updateOrderToPaid(id)
        orderHandler.tellShippingService(id)
        return ResponseEntity.ok(updated)
    }
}
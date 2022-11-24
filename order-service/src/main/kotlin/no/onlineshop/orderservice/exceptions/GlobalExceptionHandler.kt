package no.onlineshop.orderservice.exceptions

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [OrderException::class])
    fun orderException(ex: OrderException): ResponseEntity<String> {
        println("ERROR: " + ex.message)
        return ResponseEntity.internalServerError().body(ex.message)
    }
}
package no.onlineshop.paymentservice.exceptions

import org.postgresql.util.PSQLException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [PSQLException::class])
    fun psqlException(ex: PSQLException): ResponseEntity<String>{
        println("ERROR: " + ex.message)
        return ResponseEntity.internalServerError().body("Store error")
    }
}
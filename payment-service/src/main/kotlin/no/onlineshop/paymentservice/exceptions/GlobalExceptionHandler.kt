package no.onlineshop.paymentservice.exceptions

import org.postgresql.util.PSQLException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [PSQLException::class])
    fun psqlException(ex: PSQLException): ResponseEntity<String> {
        println("ERROR: " + ex.message)
        return ResponseEntity.internalServerError().body("Store error")
    }

    @ExceptionHandler(value = [HttpClientErrorException::class])
    fun clientException(ex: HttpClientErrorException): ResponseEntity<String> {
        if (ex.statusCode == HttpStatus.NOT_FOUND)
            return ResponseEntity("You can not pay for this order, it does not exist!", HttpStatus.BAD_REQUEST)
        return ResponseEntity.internalServerError().build()
    }
}
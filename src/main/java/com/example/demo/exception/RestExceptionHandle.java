package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestExceptionHandle {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleNonExistingHero(HttpServletRequest request,
                                                   RuntimeException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new Response(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class Response {
    private String errorMes;
    private HttpStatus httpStatus;
}
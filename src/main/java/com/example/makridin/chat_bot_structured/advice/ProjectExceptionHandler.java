package com.example.makridin.chat_bot_structured.advice;

import com.example.makridin.chat_bot_structured.excpetion.MovieNotSearchedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ProjectExceptionHandler {

    @ExceptionHandler(MovieNotSearchedException.class)
    public ResponseEntity<Map<String, String>> handleMovieNotSearchedException(MovieNotSearchedException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Invalid response",
                        "message", ex.getMessage()
                ));
    }
}

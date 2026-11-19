package com.atividade.catalogo_livros.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErroResposta(String timestamp, int status, String erro) {}

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        return new ErroResposta(
                Instant.now().toString(),
                404,
                ex.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta tratarRequisicaoInvalida(IllegalArgumentException ex) {
        return new ErroResposta(
                Instant.now().toString(),
                400,
                ex.getMessage()
        );
    }
}
package ru.andnovikov.sportnow.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such element")
public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException() {}

}

package com.manoj.article_hub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

/**
 * Exception returned when entity was not found in database
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity was not found")
@Getter
public class NotFoundException extends RuntimeException {

    private String entityName;
    private String propertyName;
    private Object propertyValue;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String entityName, String propertyName, Object propertyValue) {
        super(String.join("%s not found with %s: %s", entityName, propertyName, propertyValue.toString()));
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }
}

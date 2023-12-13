package com.taskmanagement.taskmanagement.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundExceptionForStatus extends RuntimeException {

    String resourceName;
    String fieldName;
    String status;

    public ResourceNotFoundExceptionForStatus(String resourceName, String fieldName, String status) {
        super(String.format("%s not found with %s : %s", resourceName, fieldName,status));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.status = status;
    }
}

package com.manoj.article_hub.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiErrorResponse {
    private String status;
    private String message;
}

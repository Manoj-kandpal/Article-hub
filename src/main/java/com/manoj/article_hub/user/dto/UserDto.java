package com.manoj.article_hub.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
}

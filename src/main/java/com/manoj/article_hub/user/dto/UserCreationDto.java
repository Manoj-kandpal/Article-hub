package com.manoj.article_hub.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationDto {
    private String firstName;
    private String LastName;
    private String email;
    private String password;
}

package com.manoj.article_hub.article.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ArticleDto {
    private String title;
    private String description;
    private int numberOfLikes;
    private String importance;
    private LocalDateTime creationDateTime;
}

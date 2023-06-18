package com.manoj.article_hub.article.dto;

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
public class CreateArticleDto {
    private String title;
    private String description;
    private String importance;
    private Long userId;
}

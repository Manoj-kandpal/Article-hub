package com.manoj.article_hub.dto;

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
public class ArticleDto {
    private long id;
    private String title;
}

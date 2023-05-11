package com.manoj.article_hub.converter;

import org.springframework.stereotype.Component;

import com.manoj.article_hub.dto.ArticleDto;
import com.manoj.article_hub.entity.ArticleEntity;

@Component
public class ArticleConverter {

    public ArticleDto convertToDto(ArticleEntity entity) {
        return ArticleDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .build();
    }
}

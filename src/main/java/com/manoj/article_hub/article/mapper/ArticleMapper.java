package com.manoj.article_hub.article.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.manoj.article_hub.article.dto.ArticleDto;
import com.manoj.article_hub.article.dto.CreateArticleDto;
import com.manoj.article_hub.article.entity.ArticleEntity;
import com.manoj.article_hub.common.DateTimeService;
import com.manoj.article_hub.common.HasLogger;
import com.manoj.article_hub.common.MapperConfig;
import com.manoj.article_hub.user.entity.UserEntity;

@Mapper(config = MapperConfig.class)
public abstract class ArticleMapper implements HasLogger {

    @Autowired
    private DateTimeService dateTimeService;

    public abstract ArticleDto toDto(ArticleEntity articleEntity);

    public ArticleEntity toEntity(CreateArticleDto createArticleDto, UserEntity creationUser) {
        return ArticleEntity.builder()
                .title(createArticleDto.getTitle())
                .description(createArticleDto.getDescription())
                .creationDateTime(dateTimeService.getCurrentDateTimeLocal())
                .updateDateTime(dateTimeService.getCurrentDateTimeLocal())
                .importance(createArticleDto.getImportance())
                .createdBy(creationUser)
                .build();
    }

}

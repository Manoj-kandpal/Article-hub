package com.manoj.article_hub.article.utils;


import com.manoj.article_hub.article.dto.CreateArticleDto;
import com.manoj.article_hub.article.entity.ArticleEntity;
import com.manoj.article_hub.user.entity.UserEntity;
import com.manoj.article_hub.user.utils.UserTestUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Utility class that provides Article related helper methods.
 */
public class ArticleTestUtil {

    public static final String ARTICLE_TITLE = "Article Title";
    public static final String ARTICLE_TITLE_TWO = "Article Title2";
    public static final String ARTICLE_DESCRIPTION = "Article Description";
    public static final String ARTICLE_DESCRIPTION_TWO = "Article Description2";
    public static final LocalDateTime CREATION_DATE = LocalDateTime.of(2023, 1,1,0,0);
    public static final LocalDateTime UPDATE_DATE = LocalDateTime.of(2023, 2,2,2,2);
    public static final Long ARTICLE_ID = 1L;
    public static final Long ARTICLE_ID_TWO = 2L;
    public static final Long USER_ID = 1L;

    public static ArticleEntity createArticle() {
        return ArticleEntity.builder()
                .id(ARTICLE_ID)
                .title(ARTICLE_TITLE)
                .description(ARTICLE_DESCRIPTION)
                .creationDateTime(CREATION_DATE)
                .updateDateTime(UPDATE_DATE)
                .createdBy(UserTestUtil.createUser())
                .build();
    }

    public static CreateArticleDto createArticleDto() {
        return CreateArticleDto.builder()
                .title(ArticleTestUtil.ARTICLE_TITLE)
                .description(ArticleTestUtil.ARTICLE_DESCRIPTION)
                .userId(USER_ID)
                .build();
    }

    public static List<ArticleEntity> createArticles() {
        ArticleEntity article_one = createArticle();
        ArticleEntity article_two = createArticle();
        article_two.setId(ARTICLE_ID_TWO);
        article_two.setTitle(ARTICLE_TITLE_TWO);
        article_two.setDescription(ARTICLE_DESCRIPTION_TWO);

        return List.of(article_one, article_two);
    }

    public static List<ArticleEntity> createArticles(UserEntity user) {
        ArticleEntity article_one = createArticle(user);
        ArticleEntity article_two = createArticle(user);
        article_two.setId(ARTICLE_ID_TWO);
        article_two.setTitle(ARTICLE_TITLE_TWO);
        article_two.setDescription(ARTICLE_DESCRIPTION_TWO);

        return List.of(article_one, article_two);
    }

    public static ArticleEntity createArticle(UserEntity user) {
        return ArticleEntity.builder()
                .createdBy(user)
                .id(ARTICLE_ID)
                .title(ARTICLE_TITLE)
                .description(ARTICLE_DESCRIPTION)
                .creationDateTime(CREATION_DATE)
                .updateDateTime(UPDATE_DATE)
                .build();
    }
}

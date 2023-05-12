package com.manoj.article_hub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manoj.article_hub.dto.ArticleInputDto;
import com.manoj.article_hub.entity.ArticleEntity;
import com.manoj.article_hub.exception.NotFoundException;
import com.manoj.article_hub.repository.ArticleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArticleService {

    private static final String ARTICLE_IS_NOT_AVAILABLE = "Article with ID {} is not available.";

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private LocalDateTimeService localDateTimeService;

    public List<ArticleEntity> getAllArticles() {
        return articleRepository.findAll();
    }

    public ArticleEntity create(ArticleInputDto data) {
        if (data == null) {
            log.error("Article can't be created if it is null");
            return null;
        }
        ArticleEntity entity = convertToEntity(data);
        ArticleEntity savedArticle = articleRepository.save(entity);
        log.info("Article {} created.", savedArticle.getTitle());
        return savedArticle;
    }

    public void deleteArticle(Long id) {
        ArticleEntity dbValue = findArticleById(id);
        articleRepository.delete(dbValue);
    }

    public ArticleEntity editArticle(Long id, ArticleInputDto data) {
        if (data == null) {
            log.error("Article can't be edited if the updated data is null");
            return null;
        }
        ArticleEntity dbValue = findArticleById(id);
        updateEntity(dbValue, data);
        ArticleEntity modifiedArticle = articleRepository.save(dbValue);
        log.info("Article {} modified.", modifiedArticle.getTitle());
        return modifiedArticle;
    }

    private void updateEntity(ArticleEntity article, ArticleInputDto data) {
        article.setTitle(data.getTitle());
        article.setDescription(data.getDescription());
        article.setImportance(data.getImportance());
        article.setUpdateDateTime(localDateTimeService.getCurrentDateTime());
    }

    private ArticleEntity findArticleById(Long id) {
        Optional<ArticleEntity> dbValue = articleRepository.findById(id);
        if (dbValue.isEmpty()) {
            log.error(ARTICLE_IS_NOT_AVAILABLE, id);
            throw new NotFoundException("Article", "Id", id);
        }
        return dbValue.get();
    }

    private ArticleEntity convertToEntity(ArticleInputDto articleInputDto) {
        return ArticleEntity.builder()
                .title(articleInputDto.getTitle())
                .description(articleInputDto.getDescription())
                .creationDateTime(localDateTimeService.getCurrentDateTime())
                .updateDateTime(localDateTimeService.getCurrentDateTime())
                .importance(articleInputDto.getImportance())
                .numberOfLikes(0)
                .build();
    }


}

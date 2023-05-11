package com.manoj.article_hub.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manoj.article_hub.dto.ArticleInputDto;
import com.manoj.article_hub.entity.ArticleEntity;
import com.manoj.article_hub.repository.ArticleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArticleService {

    private static final String ARTICLE_IS_NOT_AVAILABLE = "Article with ID {} is not available.";

    @Autowired
    private ArticleRepository articleRepository;

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
        if (dbValue == null) {
            log.warn(ARTICLE_IS_NOT_AVAILABLE, id);
            return;
        }
        articleRepository.delete(dbValue);
    }

    private ArticleEntity findArticleById(Long id) {
        Optional<ArticleEntity> dbValue = articleRepository.findById(id);
        if (dbValue.isEmpty()) {
            log.error(ARTICLE_IS_NOT_AVAILABLE, id);
            return null;
        }
        return dbValue.get();
    }

//    private void sendEvent(EventType eventType, ArticleDto dto) {
//        if (dto == null) {
//            return;
//        }
//    }
//
//
//    private void sendEvent(EventType eventType, ArticleEntity articleEntity) {
//        sendEvent(eventType, converter.convertToDto(articleEntity));
//    }

    private ArticleEntity convertToEntity(ArticleInputDto articleInputDto) {
        return ArticleEntity.builder()
                .title(articleInputDto.getTitle())
                .description(articleInputDto.getDescription())
                .creationDate(LocalDateTime.now())
                .importance(articleInputDto.getImportance())
                .numberOfLikes(articleInputDto.getNumberOfLikes())
                .build();
    }

}

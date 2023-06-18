package com.manoj.article_hub.article.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manoj.article_hub.article.dto.CreateArticleDto;
import com.manoj.article_hub.article.entity.ArticleEntity;
import com.manoj.article_hub.article.mapper.ArticleMapper;
import com.manoj.article_hub.article.repository.ArticleRepository;
import com.manoj.article_hub.common.DateTimeService;
import com.manoj.article_hub.common.HasLogger;
import com.manoj.article_hub.exception.NotFoundException;
import com.manoj.article_hub.user.entity.UserEntity;
import com.manoj.article_hub.user.service.UserService;

@Service
public class ArticleService implements HasLogger {

    private static final String ARTICLE_IS_NOT_AVAILABLE = "Article with ID {} is not available.";
    private static final String ARTICLE_DELETED = "Article with title {} deleted.";
    private static final String USER_DOES_NOT_EXIST = "User with id {} doesn't exist.";

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private DateTimeService dateTimeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleMapper mapper;

    @Transactional(readOnly = true)
    public List<ArticleEntity> getAllArticles() {
        return articleRepository.findAll();
    }

    @Transactional
    public ArticleEntity create(CreateArticleDto data) {
        if (data == null) {
            getLogger().error("Article can't be created if data is null");
            return null;
        }
        //to add validations that our data has the non-null fields or not!!
        Optional<UserEntity> author = userService.checkUserExist(data.getUserId());
        if (author.isPresent()) {
            ArticleEntity entity = mapper.toEntity(data, author.get());
            ArticleEntity savedArticle = articleRepository.saveAndFlush(entity);
            getLogger().info("Article {} created.", savedArticle.getTitle());
            return savedArticle;
        } else {
            getLogger().error(USER_DOES_NOT_EXIST, data.getUserId());
            throw new NotFoundException("User", "Id", data.getUserId());
        }

    }

    @Transactional
    public Optional<ArticleEntity> deleteArticle(Long id) {
        Optional<ArticleEntity> dbValue = findArticleById(id);
        dbValue.ifPresent(articleEntity -> {
            articleRepository.delete(articleEntity);
            getLogger().info(ARTICLE_DELETED, articleEntity.getTitle());
        });
        return dbValue;
    }

    @Transactional
    public ArticleEntity editArticle(Long id, CreateArticleDto data) {
        if (data == null) {
            getLogger().error("Article can't be edited if the updated data is null");
            return null;
        }
        return articleRepository.findById(id).map(articleEntity -> {
            updateEntity(articleEntity, data);
            ArticleEntity modifiedArticle = articleRepository.save(articleEntity);
            getLogger().info("Article {} modified.", modifiedArticle.getTitle());
            return modifiedArticle;
        }).orElse(null);
    }

    private void updateEntity(ArticleEntity article, CreateArticleDto data) {
        article.setTitle(data.getTitle());
        article.setDescription(data.getDescription());
        article.setImportance(data.getImportance());
        article.setUpdateDateTime(dateTimeService.getCurrentDateTimeLocal());
    }

    @Transactional(readOnly = true)
    public Optional<ArticleEntity> findArticleById(Long id) {
        return Optional.of(articleRepository.findById(id).orElseThrow(() -> {
            getLogger().error(ARTICLE_IS_NOT_AVAILABLE, id);
            return new NotFoundException("Article", "Id", id);
        }));
    }
}

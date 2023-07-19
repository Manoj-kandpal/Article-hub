package com.manoj.article_hub.article.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manoj.article_hub.article.dto.ArticleDto;
import com.manoj.article_hub.article.dto.CreateArticleDto;
import com.manoj.article_hub.article.entity.ArticleEntity;
import com.manoj.article_hub.article.mapper.ArticleMapper;
import com.manoj.article_hub.article.service.ArticleService;
import com.manoj.article_hub.common.HasLogger;

@RestController
@RequestMapping(ArticleController.ENDPOINT_PATH_ROOT)
public class ArticleController implements HasLogger {

    public static final String ENDPOINT_PATH_ROOT = "/api/article";
    public static final String ENDPOINT_PATH_ARTICLE = "/{articleId}";
    public static final String ARTICLE_ID_CANT_BE_NULL = "Article id can't be null. Please provide a valid article id.";

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping
    public List<ArticleEntity> getAllArticles() {
        return articleService.getAllArticles();
    }

    @PostMapping
    public ArticleEntity createArticle(@RequestBody CreateArticleDto createArticleDto) {
        return articleService.create(createArticleDto);
    }

    @DeleteMapping(ENDPOINT_PATH_ARTICLE)
    public ResponseEntity<ArticleDto> deleteArticle(@PathVariable(name = "articleId") Long id) {
        if (id == null) {
            getLogger().warn(ARTICLE_ID_CANT_BE_NULL);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<ArticleEntity> deletedArticle = articleService.deleteArticle(id);
        if (deletedArticle.isPresent()) {
            ArticleDto articleDto = articleMapper.toDto(deletedArticle.get());
            return new ResponseEntity<>(articleDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(ENDPOINT_PATH_ARTICLE)
    public ResponseEntity<ArticleDto> editArticle(@PathVariable(name = "articleId") Long id, @RequestBody CreateArticleDto createArticleDto) {
        if (id == null) {
            getLogger().warn(ARTICLE_ID_CANT_BE_NULL);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ArticleEntity modifiedArticle = articleService.editArticle(id, createArticleDto);
        if (modifiedArticle != null) {
            ArticleDto articleDto = articleMapper.toDto(modifiedArticle);
            return new ResponseEntity<>(articleDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(ENDPOINT_PATH_ARTICLE)
    public ResponseEntity<ArticleDto> findByNo(@PathVariable Long articleId) {
        Optional<ArticleEntity> articleEntity = articleService.findArticleById(articleId);
        if (articleEntity.isPresent()) {
            ArticleDto articleDto = articleMapper.toDto(articleEntity.get());
            return new ResponseEntity<>(articleDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

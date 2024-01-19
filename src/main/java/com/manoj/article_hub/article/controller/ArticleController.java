package com.manoj.article_hub.article.controller;

import com.manoj.article_hub.article.dto.ArticleDto;
import com.manoj.article_hub.article.dto.CreateArticleDto;
import com.manoj.article_hub.article.entity.ArticleEntity;
import com.manoj.article_hub.article.mapper.ArticleMapper;
import com.manoj.article_hub.article.service.ArticleService;
import com.manoj.article_hub.common.HasLogger;
import com.manoj.article_hub.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ArticleController.ENDPOINT_PATH_ROOT)
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Article")
public class ArticleController implements HasLogger {

    public static final String ENDPOINT_PATH_ROOT = "/api/article";
    public static final String ENDPOINT_PATH_ARTICLE = "/{articleId}";
    public static final String ARTICLE_ID_CANT_BE_NULL = "Article id can't be null. Please provide a valid article id.";
    public static final String ARTICLE_NOT_FOUND = "Article not found. Please provide a valid article id.";

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping
    public List<ArticleEntity> getAllArticles() {
        return articleService.getAllArticles();
    }

    @Operation(
            description = "Post endpoint for creating an article",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = com.manoj.article_hub.common.dto.ApiResponse.class),
                                    examples = @ExampleObject(
                                            value = "{\n  \"status\": \"success\",\n  \"data\": {}\n}"
                                    )
                            )
                    )
            }

    )
    @PostMapping
    public ResponseEntity<Object> createArticle(@RequestBody CreateArticleDto createArticleDto) {
        try {
            ArticleEntity createdArticle = articleService.create(createArticleDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new com.manoj.article_hub.common.dto.ApiResponse("success", createdArticle));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new com.manoj.article_hub.common.dto.ApiErrorResponse("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new com.manoj.article_hub.common.dto.ApiErrorResponse("error",e.getMessage()));
        }
    }

    @DeleteMapping(ENDPOINT_PATH_ARTICLE)
    public ResponseEntity<Object> deleteArticle(@PathVariable(name = "articleId") Long id) {
        if (id == null) {
            getLogger().warn(ARTICLE_ID_CANT_BE_NULL);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new com.manoj.article_hub.common.dto.ApiErrorResponse("error", ARTICLE_ID_CANT_BE_NULL));
        }
        Optional<ArticleEntity> deletedArticle = articleService.deleteArticle(id);
        if (deletedArticle.isPresent()) {
            ArticleDto articleDto = articleMapper.toDto(deletedArticle.get());
            return ResponseEntity.status(HttpStatus.OK).body(new com.manoj.article_hub.common.dto.ApiResponse("success", articleDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new com.manoj.article_hub.common.dto.ApiErrorResponse("error", ARTICLE_NOT_FOUND));
    }

    @PutMapping(ENDPOINT_PATH_ARTICLE)
    public ResponseEntity<Object> editArticle(@PathVariable(name = "articleId") Long id, @RequestBody CreateArticleDto createArticleDto) {
        if (id == null) {
            getLogger().warn(ARTICLE_ID_CANT_BE_NULL);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new com.manoj.article_hub.common.dto.ApiErrorResponse("error", ARTICLE_ID_CANT_BE_NULL));
        }
        ArticleEntity modifiedArticle = articleService.editArticle(id, createArticleDto);
        if (modifiedArticle != null) {
            ArticleDto articleDto = articleMapper.toDto(modifiedArticle);
            return ResponseEntity.status(HttpStatus.OK).body(new com.manoj.article_hub.common.dto.ApiResponse("success", articleDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new com.manoj.article_hub.common.dto.ApiErrorResponse("error", ARTICLE_NOT_FOUND));
    }

    @GetMapping(ENDPOINT_PATH_ARTICLE)
    public ResponseEntity<Object> findByNo(@PathVariable Long articleId) {
        Optional<ArticleEntity> articleEntity = articleService.findArticleById(articleId);
        if (articleEntity.isPresent()) {
            ArticleDto articleDto = articleMapper.toDto(articleEntity.get());
            return ResponseEntity.status(HttpStatus.OK).body(new com.manoj.article_hub.common.dto.ApiResponse("success", articleDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new com.manoj.article_hub.common.dto.ApiErrorResponse("error", ARTICLE_NOT_FOUND));
    }

    @GetMapping(path = "/hello")
    @Hidden
//    to hide from swagger documentation
    public String hello() {
        return "hello";
    }
}

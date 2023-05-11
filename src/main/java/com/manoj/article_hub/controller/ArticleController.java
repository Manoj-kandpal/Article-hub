package com.manoj.article_hub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manoj.article_hub.dto.ArticleInputDto;
import com.manoj.article_hub.entity.ArticleEntity;
import com.manoj.article_hub.service.ArticleService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/articles/")
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping(path = "get/allArticles")
    public List<ArticleEntity> getAllArticles() {
        return articleService.getAllArticles();
    }

    @PostMapping(path = "create")
    @Transactional
    public ArticleEntity createArticle(@RequestBody ArticleInputDto articleInputDto) {
        return articleService.create(articleInputDto);
    }

    @DeleteMapping(path = "delete")
    public void deleteArticle(@RequestParam("aid") Long id) {
        if (id == null) {
            log.warn("Article with the ID {} cannot be deleted.", id);
            return;
        }
        articleService.deleteArticle(id);
    }

}

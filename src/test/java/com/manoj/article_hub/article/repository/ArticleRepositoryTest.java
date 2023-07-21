package com.manoj.article_hub.article.repository;

import com.manoj.article_hub.IntegrationTest;
import com.manoj.article_hub.article.entity.ArticleEntity;
import com.manoj.article_hub.article.utils.ArticleTestUtil;
import com.manoj.article_hub.user.entity.UserEntity;
import com.manoj.article_hub.user.repository.UserRepository;
import com.manoj.article_hub.user.utils.UserTestUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ArticleRepositoryTest extends IntegrationTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    //no need to write test cases for the inbuilt-methods. Writing only a single test case.
    @Test
    public void testGetAllArticles() {
        UserEntity user = UserTestUtil.createUser();
        user = userRepository.saveAndFlush(user);

        ArticleEntity article1 = ArticleTestUtil.createArticle(user);
        ArticleEntity article2 = ArticleTestUtil.createArticle(user);
        article2.setDescription("Description 2");
        article2.setId(2L);

        articleRepository.saveAndFlush(article1);
        articleRepository.saveAndFlush(article2);


        List<ArticleEntity> result = articleRepository.findAll();

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
    }

}

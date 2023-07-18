package com.manoj.article_hub.article;

import com.manoj.article_hub.article.dto.CreateArticleDto;
import com.manoj.article_hub.article.entity.ArticleEntity;
import com.manoj.article_hub.article.mapper.ArticleMapper;
import com.manoj.article_hub.article.repository.ArticleRepository;
import com.manoj.article_hub.article.service.ArticleService;
import com.manoj.article_hub.article.utils.ArticleTestUtil;
import com.manoj.article_hub.exception.NotFoundException;
import com.manoj.article_hub.user.entity.UserEntity;
import com.manoj.article_hub.user.service.UserService;
import com.manoj.article_hub.user.utils.UserTestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceTest {

    public static final Long USER_ID = 1L;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserService userService;

    @Mock
    private ArticleMapper articleMapper;

    @InjectMocks
    private ArticleService testee;

    @Before
    public void init() {
        ArticleEntity article = ArticleTestUtil.createArticle();

        Mockito.when(articleRepository.findAll()).thenReturn(List.of(article));
        Mockito.when(userService.checkUserExist(article.getCreatedBy().getId())).thenReturn(Optional.of(article.getCreatedBy()));
    }

    @Test
    public void testGetAllArticles() {
        List<ArticleEntity> articles = testee.getAllArticles();
        Assert.assertNotNull(articles);
        Assert.assertEquals(1, articles.size());
    }

    @Test
    public void testCreateArticleWhenNoDataIsProvided() {
        ArticleEntity createdArticle = testee.create(null);
        Assert.assertNull(createdArticle);
    }

    @Test(expected = NotFoundException.class)
    public void testCreateArticleWhenUserDoNotExists() {
        CreateArticleDto articleDto = ArticleTestUtil.createArticleDto();

        Mockito.when(userService.checkUserExist(USER_ID)).thenReturn(Optional.empty());

        testee.create(articleDto);
    }

    @Test
    public void testCreateArticle() {
        CreateArticleDto articleDto = ArticleTestUtil.createArticleDto();
        UserEntity user = UserTestUtil.createUser();
        ArticleEntity article = ArticleTestUtil.createArticle();

        Mockito.when(userService.checkUserExist(articleDto.getUserId())).thenReturn(Optional.of(user));
        Mockito.when(articleMapper.toEntity(Mockito.eq(articleDto), Mockito.eq(user))).thenReturn(article);
        Mockito.when(articleRepository.saveAndFlush(Mockito.eq(article))).thenReturn(article);

        ArticleEntity result = testee.create(articleDto);

        Assert.assertNotNull(result);
    }
}

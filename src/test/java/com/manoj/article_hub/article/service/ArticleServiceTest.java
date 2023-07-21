package com.manoj.article_hub.article.service;

import com.manoj.article_hub.article.dto.CreateArticleDto;
import com.manoj.article_hub.article.entity.ArticleEntity;
import com.manoj.article_hub.article.mapper.ArticleMapper;
import com.manoj.article_hub.article.repository.ArticleRepository;
import com.manoj.article_hub.article.utils.ArticleTestUtil;
import com.manoj.article_hub.common.DateTimeService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceTest {

    public static final Long USER_ID = 1L;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserService userService;

    @Mock
    private ArticleMapper articleMapper;

    @Mock
    private DateTimeService dateTimeService;

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
        assertNotNull(articles);
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

        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteArticleWhenArticleNotFound() {
        Mockito.when(articleRepository.findById(eq(ArticleTestUtil.ARTICLE_ID))).thenReturn(Optional.empty());

        testee.deleteArticle(ArticleTestUtil.ARTICLE_ID);
    }

    @Test
    public void testDeleteArticle() {
        ArticleEntity article = ArticleTestUtil.createArticle();

        Mockito.when(articleRepository.findById(eq(ArticleTestUtil.ARTICLE_ID))).thenReturn(Optional.of(article));

        Optional<ArticleEntity> result = testee.deleteArticle(ArticleTestUtil.ARTICLE_ID);

        assertNotNull(result);
        Assert.assertTrue(result.isPresent());
        verify(articleRepository,Mockito.times(1)).delete(article);
    }

    @Test
    public void testEditArticleWhenNoDataIsProvided() {
        ArticleEntity result = testee.editArticle(ArticleTestUtil.ARTICLE_ID, null);

        Assert.assertNull(result);
    }

    @Test
    public void testEditArticleWhenArticleNotFound() {
        Mockito.when(articleRepository.findById(eq(ArticleTestUtil.ARTICLE_ID))).thenReturn(Optional.empty());

        ArticleEntity result = testee.editArticle(ArticleTestUtil.ARTICLE_ID, ArticleTestUtil.createArticleDto());

        Assert.assertNull(result);
    }

    @Test
    public void testEditArticle() {
        CreateArticleDto updatedArticle = ArticleTestUtil.createArticleDto();
        updatedArticle.setTitle("Updated Title");
        updatedArticle.setDescription("Updated Description.");
        ArticleEntity existingArticle = ArticleTestUtil.createArticle();
        LocalDateTime modifiedTime = LocalDateTime.of(2023,07,19,15,15);

        ArticleEntity modifiedArticle = ArticleTestUtil.createArticle();
        modifiedArticle.setTitle("Updated Title");
        modifiedArticle.setDescription("Updated Description.");
        modifiedArticle.setUpdateDateTime(modifiedTime);

        Mockito.when(articleRepository.findById(eq(ArticleTestUtil.ARTICLE_ID))).thenReturn(Optional.of(existingArticle));
        Mockito.when(dateTimeService.getCurrentDateTimeLocal()).thenReturn(modifiedTime);
        Mockito.when(articleRepository.save(eq(modifiedArticle))).thenReturn(modifiedArticle);

        ArticleEntity result = testee.editArticle(ArticleTestUtil.ARTICLE_ID, updatedArticle);

        verify(articleRepository, times(1)).save(eq(modifiedArticle));
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description.", result.getDescription());
        assertEquals(modifiedTime, result.getUpdateDateTime());
    }
}

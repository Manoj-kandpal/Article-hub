package com.manoj.article_hub.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manoj.article_hub.user.entity.UserCredentialsEntity;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentialsEntity, Long> {
}

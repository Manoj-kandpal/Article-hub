package com.manoj.article_hub.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class LocalDateTimeService {

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}

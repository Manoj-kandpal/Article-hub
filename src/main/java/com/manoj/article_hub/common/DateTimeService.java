package com.manoj.article_hub.common;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class DateTimeService {

    public LocalDateTime getCurrentDateTimeLocal() {
        return LocalDateTime.now();
    }
}

package com.redis.hgz.redis;

import java.util.Date;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateTest {
    @Test
    public void testDate() {
        log.info("now:{}", new Date());
    }
}

package com.personal.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class RateLimitConfig {

    @Value("${app.ratelimit.capacity}")
    private int capacity;

    @Value("${app.ratelimit.time}")
    private int time;

    @Value("${app.ratelimit.unit:MINUTES}")
    private String timeUnit;

    @Bean
    public Bucket authenticationBucket() {
        Duration duration = Duration.of(time, java.time.temporal.ChronoUnit.valueOf(timeUnit));
        Bandwidth limit = Bandwidth.classic(capacity, Refill.greedy(capacity, duration));
        return Bucket4j.builder().addLimit(limit).build();
    }
}

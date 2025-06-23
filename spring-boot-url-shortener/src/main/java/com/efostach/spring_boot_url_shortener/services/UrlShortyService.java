package com.efostach.spring_boot_url_shortener.services;

import com.efostach.spring_boot_url_shortener.exceptions.HashNotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UrlShortyService {
    private final RedisTemplate<String, String> redis;
    private final MessageDigest digest;

    public UrlShortyService(RedisTemplate<String, String> redis) throws NoSuchAlgorithmException {
        this.redis = redis;
        this.digest = MessageDigest.getInstance("SHA-256");
    }

    public String shorten(String url) {
        String hash = hash(url);
        redis.opsForValue().set(hash, url);

        return hash;
    }

    private String hash(String url) {
        return hash(url, 6);
    }

    private String hash(String url, int length) {
        byte[] bytes = digest.digest(url.getBytes(StandardCharsets.UTF_8));
        String hash = String.format("%032x", new BigInteger(1, bytes));

        return hash.substring(0, Math.min(length, hash.length()));
    }

    public String resolve(String hash) {
        String url = redis.opsForValue().get(hash);
        if (url == null) {
            throw new HashNotFoundException(hash);
        }
        return url;
    }
}

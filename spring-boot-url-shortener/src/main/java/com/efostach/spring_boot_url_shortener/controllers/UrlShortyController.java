package com.efostach.spring_boot_url_shortener.controllers;

import com.efostach.spring_boot_url_shortener.data.UrlShortyRequest;
import com.efostach.spring_boot_url_shortener.data.UrlShortyResponse;
import com.efostach.spring_boot_url_shortener.exceptions.HashNotFoundException;
import com.efostach.spring_boot_url_shortener.services.UrlShortyService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlShortyController {

    private final UrlShortyService urlShortyService;

    public UrlShortyController(UrlShortyService urlShortyService) {
        this.urlShortyService = urlShortyService;
    }

    @PostMapping("/")
    public UrlShortyResponse shorten(@RequestBody UrlShortyRequest request) {
        String hash = urlShortyService.shorten(request.url());

        if (hash == null || hash.isBlank()) {
            throw new HashNotFoundException("Hash not found");
        }

        return new UrlShortyResponse(hash);
    }

    @GetMapping("/{hash}")
    public ResponseEntity<Void> resolve(@PathVariable String hash) {
        String target = urlShortyService.resolve(hash);

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create(target))
                .header(HttpHeaders.CONNECTION, "close")
                .build();
    }
}

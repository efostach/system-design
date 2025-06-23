package com.efostach.spring_boot_url_shortener.exceptions;

public class HashNotFoundException extends RuntimeException {
    public HashNotFoundException(String hash) {
        super("Hash not found: " + hash);
    }
}

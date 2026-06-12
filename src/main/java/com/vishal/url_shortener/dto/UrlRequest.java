package com.vishal.url_shortener.dto;

import lombok.Data;

@Data
public class UrlRequest {

    private String originalUrl;
    private String customAlias;
}
package com.vishal.url_shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortenResponse {

    private String shortCode;
    private String shortUrl;
}
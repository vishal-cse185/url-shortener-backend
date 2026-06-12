package com.vishal.url_shortener.service;

import com.vishal.url_shortener.entity.Url;
import com.vishal.url_shortener.repository.UrlRepository;
import com.vishal.url_shortener.util.Base62Encoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlService {

    private final UrlRepository repository;

    public UrlService(UrlRepository repository) {
        this.repository = repository;
    }

    public String shortenUrl(String originalUrl, String customAlias) {

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setCategory(detectCategory(originalUrl));
        url.setRiskLevel(detectRisk(originalUrl));

        if (customAlias != null && !customAlias.isBlank()) {

            if (repository.findByCustomAlias(customAlias).isPresent()) {
                throw new RuntimeException("Custom alias already exists");
            }

            url.setShortCode(customAlias);
            url.setCustomAlias(customAlias);

            repository.save(url);

            return customAlias;
        }

        Url saved = repository.save(url);

        String shortCode = Base62Encoder.encode(saved.getId());

        saved.setShortCode(shortCode);

        repository.save(saved);

        return shortCode;
    }

    private String detectCategory(String url) {

        url = url.toLowerCase();

        if (url.contains("udemy") || url.contains("coursera") || url.contains("edx")) {
            return "Education";
        }

        if (url.contains("amazon") || url.contains("flipkart")) {
            return "Shopping";
        }

        if (url.contains("youtube") || url.contains("netflix")) {
            return "Entertainment";
        }

        if (url.contains("github") || url.contains("stackoverflow")) {
            return "Technology";
        }

        if (url.contains("linkedin")) {
            return "Professional";
        }

        return "General";
    }

    private String detectRisk(String url) {

        url = url.toLowerCase();

        if (url.contains("free-money")
                || url.contains("win-prize")
                || url.contains("lottery")
                || url.contains(".xyz")
                || url.contains("click-here")) {

            return "HIGH";
        }

        if (url.contains("bit.ly")
                || url.contains("tinyurl")) {

            return "MEDIUM";
        }

        return "LOW";
    }

    public Url getOriginalUrl(String shortCode) {

        Url url = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        url.setClickCount(url.getClickCount() + 1);

        repository.save(url);

        return url;
    }

    public Url getStats(String shortCode) {

        return repository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));
    }

    public List<Url> getAllUrls() {
        return repository.findAll();
    }
}
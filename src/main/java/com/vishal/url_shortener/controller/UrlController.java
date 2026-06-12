package com.vishal.url_shortener.controller;

import com.vishal.url_shortener.dto.ShortenResponse;
import com.vishal.url_shortener.dto.UrlRequest;
import com.vishal.url_shortener.entity.Url;
import com.vishal.url_shortener.service.QrCodeService;
import com.vishal.url_shortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/url")
public class UrlController {

    private final UrlService service;
    private final QrCodeService qrCodeService;

    public UrlController(
            UrlService service,
            QrCodeService qrCodeService) {

        this.service = service;
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/shorten")
    public ShortenResponse shorten(
            @RequestBody UrlRequest request) {

        String shortCode = service.shortenUrl(
                request.getOriginalUrl(),
                request.getCustomAlias()
        );

        return new ShortenResponse(
                shortCode,
                "http://localhost:8080/api/url/" + shortCode
        );
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode) {

        Url url = service.getOriginalUrl(shortCode);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url.getOriginalUrl()))
                .build();
    }

    @GetMapping("/stats/{shortCode}")
    public Url getStats(
            @PathVariable String shortCode) {

        return service.getStats(shortCode);
    }

    @GetMapping("/all")
    public List<Url> getAllUrls() {
        return service.getAllUrls();
    }

    @GetMapping(
            value = "/qr/{shortCode}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] generateQrCode(
            @PathVariable String shortCode)
            throws Exception {

        String shortUrl =
                "http://localhost:8080/api/url/"
                        + shortCode;

        return qrCodeService.generateQrCode(shortUrl);
    }
}
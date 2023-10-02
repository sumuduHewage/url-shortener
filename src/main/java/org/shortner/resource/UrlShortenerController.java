package org.shortner.resource;

import org.shortner.model.UrlMappingDTO;
import org.shortner.service.UrlShortenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {
    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten-url")
    public ResponseEntity<UrlMappingDTO> shortenUrl(@RequestBody UrlMappingDTO urlMappingDTO) {
        String longUrl = urlMappingDTO.getLongUrl();
        UrlMappingDTO modifiedUrlInformation = urlShortenerService.shortenUrl(longUrl);
        return ResponseEntity.ok().body(modifiedUrlInformation);
    }

    @PostMapping("/expand-url")
    public ResponseEntity<UrlMappingDTO> expandUrl(@RequestBody UrlMappingDTO urlMappingDTO) {
        String shortUrl = urlMappingDTO.getShortUrl();
        UrlMappingDTO modifiedUrlInformation = urlShortenerService.expandUrl(shortUrl);
        return ResponseEntity.ok().body(modifiedUrlInformation);
    }
}

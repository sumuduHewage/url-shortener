package org.shortner.resource;

import org.shortner.model.UrlInformationDTO;
import org.shortner.service.UrlShortenerService;
import org.shortner.util.CommonConstants;
import org.shortner.util.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);
    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten-url")
    public ResponseEntity<UrlInformationDTO> shortenUrl(@RequestBody UrlInformationDTO urlInformationDTO) {
        String longUrl = urlInformationDTO.getLongUrl();

        if (longUrl == null || longUrl.trim().isEmpty()) {
            logger.debug("Url not found : {}", CommonConstants.URL_NOT_FOUND);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UrlInformationDTO());
        }

        if (!UrlValidator.isValid(longUrl)) {
            logger.debug("Invalid url : {}", CommonConstants.INVALID_URL_FORMAT);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UrlInformationDTO());
        }

        UrlInformationDTO modifiedUrlInformation = urlShortenerService.shortenUrl(longUrl);
        return ResponseEntity.ok().body(modifiedUrlInformation);
    }

    @PostMapping("/expand-url")
    public ResponseEntity<UrlInformationDTO> expandUrl(@RequestBody UrlInformationDTO urlInformationDTO) {
        String shortUrl = urlInformationDTO.getShortUrl();

        if (shortUrl == null || shortUrl.trim().isEmpty()) {
            logger.debug("url not found : {}", CommonConstants.URL_IS_EMPTY);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UrlInformationDTO());
        }

        if (!UrlValidator.isValid(shortUrl)) {
            logger.debug("Invalid url : {}", CommonConstants.INVALID_URL_FORMAT);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UrlInformationDTO());
        }

        UrlInformationDTO modifiedUrlInformation = urlShortenerService.expandUrl(shortUrl);
        return ResponseEntity.ok().body(modifiedUrlInformation);
    }
}

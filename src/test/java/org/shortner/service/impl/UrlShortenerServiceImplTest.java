package org.shortner.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shortner.configuration.ApplicationConfiguration;
import org.shortner.exception.InvalidURLException;
import org.shortner.model.UrlMappingDTO;
import org.shortner.model.entity.UrlMapping;
import org.shortner.repository.UrlMappingRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UrlShortenerServiceImplTest {

    @InjectMocks
    private UrlShortenerServiceImpl urlShortenerService;

    @Mock
    private ApplicationConfiguration applicationConfiguration;

    @Mock
    private UrlMappingRepository urlMappingRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        urlShortenerService = new UrlShortenerServiceImpl(applicationConfiguration, urlMappingRepository);
    }

    @Test
    void testShortenUrlSuccess() {
        when(applicationConfiguration.getBaseUrl()).thenReturn("http://example.com/");
        when(urlMappingRepository.findByOriginalUrl(any())).thenReturn(null);

        UrlMappingDTO result = urlShortenerService.shortenUrl("http://example.com/long-url");

        // Assertions
        assertNotNull(result);
        assertEquals("http://example.com/tjvQY2", result.getShortUrl());
    }

    @Test
    void testShortenUrlExistingMapping() {
        when(applicationConfiguration.getBaseUrl()).thenReturn("http://example.com/");

        UrlMapping existingMapping = new UrlMapping();
        existingMapping.setShortUrl("http://example.com/existing-short-url");

        // Mock the behavior of urlMappingRepository to return the existing mapping
        when(urlMappingRepository.findByOriginalUrl(any())).thenReturn(existingMapping);

        UrlMappingDTO result = urlShortenerService.shortenUrl("http://example.com/long-url");

        // Assertions
        assertNotNull(result);
        assertEquals("http://example.com/existing-short-url", result.getShortUrl());
    }

    @Test
    void testExpandUrlSuccess() {
        UrlMapping sampleMapping = new UrlMapping();
        sampleMapping.setShortUrl("http://example.com/short-url");
        sampleMapping.setOriginalUrl("http://example.com/long-url");

        when(urlMappingRepository.findByShortUrl(any())).thenReturn(Optional.of(sampleMapping));

        UrlMappingDTO result = urlShortenerService.expandUrl("http://example.com/short-url");

        // Assertions
        assertNotNull(result);
        assertEquals("http://example.com/long-url", result.getLongUrl());
    }


    @Test
    void testExpandUrlNotFound() {
        when(urlMappingRepository.findByShortUrl(any())).thenReturn(Optional.empty());

        // Assertions
        assertThrows(IllegalArgumentException.class, () -> {
            urlShortenerService.expandUrl("http://example.com/non-existing-short-url");
        });
    }

    @Test
    void testShortenUrlInvalidUrl() {
        // service method with an invalid URL
        assertThrows(InvalidURLException.class, () -> {
            urlShortenerService.shortenUrl("invalid-url");
        });
    }

    @Test
    void testExpandUrlInvalidUrl() {
        // service method with an invalid URL
        assertThrows(InvalidURLException.class, () -> {
            urlShortenerService.expandUrl("invalid-url");
        });
    }
}

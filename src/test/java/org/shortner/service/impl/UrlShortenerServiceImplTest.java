package org.shortner.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shortner.configuration.ApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UrlShortenerServiceImplTest {

    private UrlShortenerServiceImpl urlShortenerService;

    @Mock
    private ApplicationConfiguration applicationConfiguration;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        urlShortenerService = new UrlShortenerServiceImpl(applicationConfiguration);
    }

    @Test
    void testShortenAndExpandUrl() {
        // Mock the applicationConfiguration to return a base URL
        when(applicationConfiguration.getBaseUrl()).thenReturn("http://localhost:8080/");

        String longUrl = "https://www.example.com/blog/i-did-something-cool";
        String shortUrl = urlShortenerService.shortenUrl(longUrl);

        assertNotNull(shortUrl);
        assertTrue(shortUrl.startsWith("http://localhost:8080/"));

        // Update this assertion to match the new format
        assertTrue(shortUrl.endsWith("/" + urlShortenerService.generateShortUrl(longUrl)));

        String expandedUrl = urlShortenerService.expandUrl(shortUrl);
        assertEquals(longUrl, expandedUrl);
    }


    @Test
    void testShortenUrlDuplicate() {
        // Mock the applicationConfiguration to return a base URL
        when(applicationConfiguration.getBaseUrl()).thenReturn("http://localhost:8080");

        String longUrl = "https://www.example.com/blog/i-did-something-cool";

        String shortUrl1 = urlShortenerService.shortenUrl(longUrl);
        String shortUrl2 = urlShortenerService.shortenUrl(longUrl);

        // Both short URLs should be the same since it's a duplicate long URL
        assertEquals(shortUrl1, shortUrl2);
    }

    @Test
    void testExpandUrlNotFound() {
        String shortUrl = "http://localhost:8080/notfound";

        assertThrows(IllegalArgumentException.class, () -> urlShortenerService.expandUrl(shortUrl));
    }
}

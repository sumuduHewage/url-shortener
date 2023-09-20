package org.shortner.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shortner.configuration.ApplicationConfiguration;
import org.shortner.model.UrlInformationDTO;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UrlShortenerServiceImplTest {
    private UrlShortenerServiceImpl urlShortenerService;

    @Mock
    private ApplicationConfiguration applicationConfiguration;

    @Mock
    private Map<String, String> shortToLongUrlMap;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        urlShortenerService = new UrlShortenerServiceImpl(applicationConfiguration);
    }

    @Test
    void testShortenUrl() {
        when(applicationConfiguration.getBaseUrl()).thenReturn("http://localhost:8080/");

        String longUrl = "https://www.example.com/blog/i-did-something-cool";
        UrlInformationDTO urlInformationDTO = urlShortenerService.shortenUrl(longUrl);

        assertNotNull(urlInformationDTO);
        assertTrue(urlInformationDTO.getShortUrl().startsWith("http://localhost:8080/"));
        assertEquals(longUrl, urlInformationDTO.getLongUrl());
    }

    @Test
    void testShortenUrlDuplicate() {
        when(applicationConfiguration.getBaseUrl()).thenReturn("http://localhost:8080/");

        String longUrl = "https://www.example.com/blog/i-did-something-cool";

        UrlInformationDTO urlInformationDTO1 = urlShortenerService.shortenUrl(longUrl);
        UrlInformationDTO urlInformationDTO2 = urlShortenerService.shortenUrl(longUrl);

        // Both short URLs should be the same since it's a duplicate long URL
        assertEquals(urlInformationDTO1.getShortUrl(), urlInformationDTO2.getShortUrl());
    }

    @Test
    void testExpandUrlWithInvalidShortUrl() {
        String shortUrl = "http://localhost:8080/invalid";

        when(shortToLongUrlMap.get(shortUrl)).thenReturn(null);

        // Call the method under test and expect an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> urlShortenerService.expandUrl(shortUrl));
    }

    @Test
    void testExpandUrlNotFound() {
        String shortUrl = "http://localhost:8080/notfound";

        assertThrows(IllegalArgumentException.class, () -> urlShortenerService.expandUrl(shortUrl));
    }
}

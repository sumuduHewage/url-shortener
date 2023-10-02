package org.shortner.service;

import org.shortner.model.UrlMappingDTO;

public interface UrlShortenerService {
    /**
     * to shorten URL using long URL
     *
     * @param longUrl longURL
     * @return  shorten URL
     */
    UrlMappingDTO shortenUrl(String longUrl);

    /**
     * to expand URL using shorten URL
     *
     * @param shortUrl shorten URL
     * @return  expand URL
     */

    UrlMappingDTO expandUrl(String shortUrl);
}

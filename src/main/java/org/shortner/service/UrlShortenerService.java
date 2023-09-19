package org.shortner.service;

public interface UrlShortenerService {
    /**
     * to shorten URL using long URL
     *
     * @param longUrl longURL
     * @return  shorten URL
     */
    String shortenUrl(String longUrl);

    /**
     * to expand URL using shorten URL
     *
     * @param shortUrl shorten URL
     * @return  expand URL
     */

    String expandUrl(String shortUrl);
}

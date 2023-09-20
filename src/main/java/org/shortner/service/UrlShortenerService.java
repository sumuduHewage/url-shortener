package org.shortner.service;

import org.shortner.model.UrlInformationDTO;

public interface UrlShortenerService {
    /**
     * to shorten URL using long URL
     *
     * @param longUrl longURL
     * @return  shorten URL
     */
    UrlInformationDTO shortenUrl(String longUrl);

    /**
     * to expand URL using shorten URL
     *
     * @param shortUrl shorten URL
     * @return  expand URL
     */

    UrlInformationDTO expandUrl(String shortUrl);
}

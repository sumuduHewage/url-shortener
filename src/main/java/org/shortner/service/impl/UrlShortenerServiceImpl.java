package org.shortner.service.impl;

import org.shortner.configuration.ApplicationConfiguration;
import org.shortner.exception.HashingAlgorithmException;
import org.shortner.service.UrlShortenerService;
import org.shortner.util.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerServiceImpl.class);
    private final ApplicationConfiguration applicationConfiguration;
    final Map<String, String> shortToLongUrlMap = new HashMap<>();
    private final Map<String, String> longToShortUrlMap = new HashMap<>();

    public UrlShortenerServiceImpl(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public String shortenUrl(String longUrl) {

        if (shortToLongUrlMap.containsValue(longUrl)) {
            return longToShortUrlMap.get(longUrl);
        }

        String shortUrl = applicationConfiguration.getBaseUrl() + generateShortUrl(longUrl);
        shortToLongUrlMap.put(shortUrl, longUrl);
        longToShortUrlMap.put(longUrl, shortUrl);
        logger.info("shorten url : {}", shortUrl);

        return shortUrl;
    }

    public String expandUrl(String shortUrl) {
        String longUrl = shortToLongUrlMap.get(shortUrl);

        if (longUrl != null) {
            logger.info("long url : {}", longUrl);
            return longUrl;
        } else {
            logger.debug(CommonConstants.URL_NOT_FOUND);
            throw new IllegalArgumentException(CommonConstants.URL_NOT_FOUND);
        }
    }

    synchronized String generateShortUrl(String longUrl) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(longUrl.getBytes());
            byte[] byteData = md.digest();

            StringBuilder shortUrl = new StringBuilder();
            for (byte b : byteData) {
                shortUrl.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }

            return shortUrl.substring(0, 6);
        } catch (NoSuchAlgorithmException e) {
            throw new HashingAlgorithmException(CommonConstants.HASHING_ALGORITHM_NOT_FOUND, e);
        }
    }
}

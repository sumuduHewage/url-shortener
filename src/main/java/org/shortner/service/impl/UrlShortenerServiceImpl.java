package org.shortner.service.impl;

import org.shortner.configuration.ApplicationConfiguration;
import org.shortner.exception.HashingAlgorithmException;
import org.shortner.exception.InvalidURLException;
import org.shortner.model.UrlMappingDTO;
import org.shortner.model.entity.UrlMapping;
import org.shortner.repository.UrlMappingRepository;
import org.shortner.service.UrlShortenerService;
import org.shortner.service.mapper.UrlMapper;
import org.shortner.util.CommonConstants;
import org.shortner.util.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerServiceImpl.class);

    private final ApplicationConfiguration applicationConfiguration;

    private final UrlMappingRepository urlMappingRepository;

    public UrlShortenerServiceImpl(ApplicationConfiguration applicationConfiguration, UrlMappingRepository urlMappingRepository) {
        this.applicationConfiguration = applicationConfiguration;
        this.urlMappingRepository = urlMappingRepository;
    }

    public UrlMappingDTO shortenUrl(String longUrl) {

        if (longUrl == null || longUrl.trim().isEmpty()) {
            logger.debug("Url not found : {}", CommonConstants.URL_NOT_FOUND);
            throw new InvalidURLException(CommonConstants.URL_NOT_FOUND);
        }

        if (!UrlValidator.isValid(longUrl)) {
            logger.debug("Invalid url : {}", CommonConstants.INVALID_URL_FORMAT);
            throw new InvalidURLException(CommonConstants.INVALID_URL_FORMAT);
        }

        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
        urlMappingDTO.setLongUrl(longUrl);

        UrlMapping existingMapping = urlMappingRepository.findByOriginalUrl(urlMappingDTO.getLongUrl());

        if (existingMapping != null) {
            urlMappingDTO.setShortUrl(existingMapping.getShortUrl());
            return urlMappingDTO;
        }

        String shortUrl = applicationConfiguration.getBaseUrl() + generateShortUrl(longUrl);
        urlMappingDTO.setShortUrl(shortUrl);

        // if record not existed in, Then save
        createUrlMapping(urlMappingDTO);

        logger.info("shorten url : {}", shortUrl);
        return urlMappingDTO;
    }

    private void createUrlMapping(UrlMappingDTO urlMappingDTO) {
        UrlMapping urlMapping = UrlMapper.INSTANCE.toEntity(urlMappingDTO);
        urlMappingRepository.save(urlMapping);
        logger.info("saved successfully");
    }

    public UrlMappingDTO expandUrl(String shortUrl) {

        if (shortUrl == null || shortUrl.trim().isEmpty()) {
            logger.debug("url not found : {}", CommonConstants.URL_IS_EMPTY);
            throw new InvalidURLException(CommonConstants.URL_IS_EMPTY);
        }

        if (!UrlValidator.isValid(shortUrl)) {
            logger.debug("Invalid url : {}", CommonConstants.INVALID_URL_FORMAT);
            throw new InvalidURLException(CommonConstants.INVALID_URL_FORMAT);
        }

        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
        urlMappingDTO.setShortUrl(shortUrl);

        // fetch short Url From table
        Optional<UrlMapping> urlMappingOpt = urlMappingRepository.findByShortUrl(shortUrl);
        if (!urlMappingOpt.isPresent()) {
            logger.debug(CommonConstants.URL_NOT_FOUND);
            throw new IllegalArgumentException(CommonConstants.URL_NOT_FOUND);
        } else {
            String originalUrl = urlMappingOpt.get().getOriginalUrl();
            urlMappingDTO.setLongUrl(originalUrl);
            logger.info("long url : {}", originalUrl);
            return urlMappingDTO;
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

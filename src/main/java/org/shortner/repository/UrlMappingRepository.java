package org.shortner.repository;

import org.shortner.model.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    /**
     *
     * Retrieve all url mappings
     * @param shortUrl short url
     * @return List<UrlMapping> List of UrlMappings
     */
    Optional<UrlMapping> findByShortUrl(String shortUrl);

    UrlMapping findByOriginalUrl(String originalUrl);
}

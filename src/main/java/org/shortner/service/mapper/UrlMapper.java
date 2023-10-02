package org.shortner.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.shortner.model.UrlMappingDTO;
import org.shortner.model.entity.UrlMapping;
@Mapper
public interface UrlMapper {

    UrlMapper INSTANCE = Mappers.getMapper(UrlMapper.class);

    @Mappings({
            @org.mapstruct.Mapping(source = "longUrl", target = "originalUrl"),
            @org.mapstruct.Mapping(source = "shortUrl", target = "shortUrl")
    })
    UrlMapping toEntity(UrlMappingDTO dto);

    @Mappings({
            @org.mapstruct.Mapping(source = "originalUrl", target = "longUrl"),
            @org.mapstruct.Mapping(source = "shortUrl", target = "shortUrl")
    })
    UrlMappingDTO toDto(UrlMapping entity);
}
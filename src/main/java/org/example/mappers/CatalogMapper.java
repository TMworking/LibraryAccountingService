package org.example.mappers;

import org.example.domain.Catalog;
import org.example.web.dto.catalog.response.CatalogResponse;
import org.example.web.dto.catalog.response.CatalogShortResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CatalogMapper {

    @Mapping(target = "parentId", source = "parent.id")
    CatalogResponse toResponse(Catalog catalog);

    CatalogShortResponse toShortResponse(Catalog catalog);
}

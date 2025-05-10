package org.example.mappers;

import org.example.domain.Catalog;
import org.example.web.dto.catalog.request.CatalogCreateRequest;
import org.example.web.dto.catalog.request.CatalogUpdateRequest;
import org.example.web.dto.catalog.response.CatalogPageResponse;
import org.example.web.dto.catalog.response.CatalogResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CatalogMapper {

    @Mapping(target = "parentId", source = "parent.id")
    CatalogResponse toResponse(Catalog catalog);

    Catalog toCatalog(CatalogCreateRequest request);

    default List<CatalogResponse> toResponseList(List<Catalog> catalogs) {
        return catalogs.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    default CatalogPageResponse toPageResponse(List<CatalogResponse> responses) {
        return new CatalogPageResponse(responses);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(@MappingTarget Catalog entity, CatalogUpdateRequest request);
}

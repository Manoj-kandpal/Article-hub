package com.manoj.article_hub.common;

import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@org.mapstruct.MapperConfig(componentModel = "spring",
        nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapperConfig {
}

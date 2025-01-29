package com.prx.mercury.api.v1.to;

import java.time.LocalDateTime;
import java.util.UUID;

public record TemplateTO(UUID id,
                         String description,
                         String location,
                         String fileFormat,
                         TemplateTypeTO templateType,
                         UUID application,
                         LocalDateTime createdAt,
                         LocalDateTime updatedAt,
                         Boolean isActive) {
}

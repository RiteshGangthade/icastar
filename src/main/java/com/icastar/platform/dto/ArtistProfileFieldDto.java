package com.icastar.platform.dto;

import lombok.Data;

@Data
public class ArtistProfileFieldDto {
    private Long id;
    private Long artistTypeFieldId;
    private String fieldName;
    private String displayName;
    private String fieldValue;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private String mimeType;
}

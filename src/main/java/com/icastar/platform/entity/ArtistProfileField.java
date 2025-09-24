package com.icastar.platform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "artist_profile_fields")
@Data
@EqualsAndHashCode(callSuper = true)
public class ArtistProfileField extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_profile_id", nullable = false)
    private ArtistProfile artistProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_type_field_id", nullable = false)
    private ArtistTypeField artistTypeField;

    @Column(name = "field_value", columnDefinition = "TEXT")
    private String fieldValue;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize; // File size in bytes

    @Column(name = "mime_type")
    private String mimeType; // File MIME type
}

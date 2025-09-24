package com.icastar.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Table(name = "artist_type_fields")
@Data
@EqualsAndHashCode(callSuper = true)
public class ArtistTypeField extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_type_id", nullable = false)
    @JsonIgnore
    private ArtistType artistType;

    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type", nullable = false)
    private FieldType fieldType;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired = false;

    @Column(name = "is_searchable", nullable = false)
    private Boolean isSearchable = false;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "validation_rules", columnDefinition = "JSON")
    private String validationRules;

    @Column(name = "options", columnDefinition = "JSON")
    private String options;

    @Column(name = "placeholder")
    private String placeholder;

    @Column(name = "help_text")
    private String helpText;

    @OneToMany(mappedBy = "artistTypeField", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArtistProfileField> artistProfileFields;

}

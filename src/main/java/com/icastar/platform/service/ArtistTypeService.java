package com.icastar.platform.service;

import com.icastar.platform.dto.ArtistTypeDto;
import com.icastar.platform.dto.ArtistTypeFieldDto;
import com.icastar.platform.entity.ArtistType;
import com.icastar.platform.entity.ArtistTypeField;
import com.icastar.platform.repository.ArtistTypeRepository;
import com.icastar.platform.repository.ArtistTypeFieldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArtistTypeService {

    private final ArtistTypeRepository artistTypeRepository;
    private final ArtistTypeFieldRepository artistTypeFieldRepository;

    @Transactional(readOnly = true)
    public List<ArtistTypeDto> getAllActiveArtistTypes() {
        return artistTypeRepository.findActiveArtistTypesOrdered()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ArtistTypeDto getArtistTypeById(Long id) {
        ArtistType artistType = artistTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist type not found with id: " + id));
        return convertToDto(artistType);
    }

    @Transactional(readOnly = true)
    public ArtistTypeDto getArtistTypeByName(String name) {
        ArtistType artistType = artistTypeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Artist type not found with name: " + name));
        return convertToDto(artistType);
    }

    @Transactional(readOnly = true)
    public List<ArtistTypeFieldDto> getArtistTypeFields(Long artistTypeId) {
        return artistTypeFieldRepository.findActiveFieldsByArtistType(artistTypeId)
                .stream()
                .map(this::convertFieldToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ArtistTypeFieldDto> getRequiredArtistTypeFields(Long artistTypeId) {
        return artistTypeFieldRepository.findByArtistTypeIdAndIsRequiredTrueOrderBySortOrder(artistTypeId)
                .stream()
                .map(this::convertFieldToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ArtistTypeFieldDto> getSearchableArtistTypeFields(Long artistTypeId) {
        return artistTypeFieldRepository.findByArtistTypeIdAndIsSearchableTrueOrderBySortOrder(artistTypeId)
                .stream()
                .map(this::convertFieldToDto)
                .collect(Collectors.toList());
    }

    public ArtistTypeDto createArtistType(ArtistTypeDto artistTypeDto) {
        ArtistType artistType = new ArtistType();
        artistType.setName(artistTypeDto.getName());
        artistType.setDisplayName(artistTypeDto.getDisplayName());
        artistType.setDescription(artistTypeDto.getDescription());
        artistType.setIconUrl(artistTypeDto.getIconUrl());
        artistType.setSortOrder(artistTypeDto.getSortOrder());
        artistType.setIsActive(true);

        ArtistType savedArtistType = artistTypeRepository.save(artistType);
        log.info("Created new artist type: {}", savedArtistType.getName());
        
        return convertToDto(savedArtistType);
    }

    public ArtistTypeFieldDto createArtistTypeField(Long artistTypeId, ArtistTypeFieldDto fieldDto) {
        ArtistType artistType = artistTypeRepository.findById(artistTypeId)
                .orElseThrow(() -> new RuntimeException("Artist type not found with id: " + artistTypeId));

        ArtistTypeField field = new ArtistTypeField();
        field.setArtistType(artistType);
        field.setFieldName(fieldDto.getFieldName());
        field.setDisplayName(fieldDto.getDisplayName());
        field.setFieldType(fieldDto.getFieldType());
        field.setIsRequired(fieldDto.getIsRequired());
        field.setIsSearchable(fieldDto.getIsSearchable());
        field.setSortOrder(fieldDto.getSortOrder());
        field.setPlaceholder(fieldDto.getPlaceholder());
        field.setHelpText(fieldDto.getHelpText());
        field.setIsActive(true);

        ArtistTypeField savedField = artistTypeFieldRepository.save(field);
        log.info("Created new field '{}' for artist type '{}'", savedField.getFieldName(), artistType.getName());
        
        return convertFieldToDto(savedField);
    }

    public ArtistTypeDto updateArtistType(Long id, ArtistTypeDto artistTypeDto) {
        ArtistType artistType = artistTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist type not found with id: " + id));

        artistType.setDisplayName(artistTypeDto.getDisplayName());
        artistType.setDescription(artistTypeDto.getDescription());
        artistType.setIconUrl(artistTypeDto.getIconUrl());
        artistType.setSortOrder(artistTypeDto.getSortOrder());
        artistType.setIsActive(artistTypeDto.getIsActive());

        ArtistType updatedArtistType = artistTypeRepository.save(artistType);
        log.info("Updated artist type: {}", updatedArtistType.getName());
        
        return convertToDto(updatedArtistType);
    }

    public void deleteArtistType(Long id) {
        ArtistType artistType = artistTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist type not found with id: " + id));
        
        artistType.setIsActive(false);
        artistTypeRepository.save(artistType);
        log.info("Deactivated artist type: {}", artistType.getName());
    }

    private ArtistTypeDto convertToDto(ArtistType artistType) {
        ArtistTypeDto dto = new ArtistTypeDto();
        dto.setId(artistType.getId());
        dto.setName(artistType.getName());
        dto.setDisplayName(artistType.getDisplayName());
        dto.setDescription(artistType.getDescription());
        dto.setIconUrl(artistType.getIconUrl());
        dto.setIsActive(artistType.getIsActive());
        dto.setSortOrder(artistType.getSortOrder());
        
        // Load fields if needed
        if (artistType.getFields() != null && !artistType.getFields().isEmpty()) {
            dto.setFields(artistType.getFields().stream()
                    .map(this::convertFieldToDto)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    private ArtistTypeFieldDto convertFieldToDto(ArtistTypeField field) {
        ArtistTypeFieldDto dto = new ArtistTypeFieldDto();
        dto.setId(field.getId());
        dto.setFieldName(field.getFieldName());
        dto.setDisplayName(field.getDisplayName());
        dto.setFieldType(field.getFieldType());
        dto.setIsRequired(field.getIsRequired());
        dto.setIsSearchable(field.getIsSearchable());
        dto.setSortOrder(field.getSortOrder());
        dto.setPlaceholder(field.getPlaceholder());
        dto.setHelpText(field.getHelpText());
        
        // TODO: Parse JSON fields for validation rules and options
        // This would require JSON parsing logic
        
        return dto;
    }
}

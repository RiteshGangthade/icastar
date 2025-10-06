package com.icastar.platform.service;

import com.icastar.platform.dto.ArtistProfileFieldDto;
import com.icastar.platform.dto.user.UpdateUserProfileDto;
import com.icastar.platform.entity.ArtistProfile;
import com.icastar.platform.entity.ArtistProfileField;
import com.icastar.platform.entity.ArtistType;
import com.icastar.platform.entity.ArtistTypeField;
import com.icastar.platform.entity.User;
import com.icastar.platform.repository.ArtistProfileFieldRepository;
import com.icastar.platform.repository.ArtistProfileRepository;
import com.icastar.platform.repository.ArtistTypeFieldRepository;
import com.icastar.platform.repository.ArtistTypeRepository;
import com.icastar.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArtistService {

    private final ArtistProfileRepository artistProfileRepository;
    private final ArtistTypeRepository artistTypeRepository;
    private final UserRepository userRepository;
    private final ArtistProfileFieldRepository artistProfileFieldRepository;
    private final ArtistTypeFieldRepository artistTypeFieldRepository;

    @Transactional(readOnly = true)
    public Optional<ArtistProfile> findById(Long id) {
        return artistProfileRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ArtistProfile> findByUserId(Long userId) {
        return artistProfileRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<ArtistProfile> findActiveArtists() {
        return artistProfileRepository.findActiveArtists();
    }

    @Transactional(readOnly = true)
    public List<ArtistProfile> findActiveVerifiedArtists() {
        return artistProfileRepository.findActiveVerifiedArtists();
    }

    @Transactional(readOnly = true)
    public Page<ArtistProfile> findMostActiveArtists(Pageable pageable) {
        return artistProfileRepository.findMostActiveArtists(pageable);
    }

    @Transactional(readOnly = true)
    public List<ArtistProfile> findByArtistTypeId(Long artistTypeId) {
        return artistProfileRepository.findByArtistTypeId(artistTypeId);
    }

    @Transactional(readOnly = true)
    public List<ArtistProfile> findByArtistTypeName(String artistTypeName) {
        return artistProfileRepository.findByArtistTypeName(artistTypeName);
    }

    @Transactional(readOnly = true)
    public List<ArtistProfile> findActiveArtistsByType(Long artistTypeId) {
        return artistProfileRepository.findActiveArtistsByType(artistTypeId);
    }

    @Transactional(readOnly = true)
    public List<ArtistProfile> findActiveArtistsByTypeName(String artistTypeName) {
        return artistProfileRepository.findActiveArtistsByTypeName(artistTypeName);
    }

    public ArtistProfile createArtistProfile(Long userId, Long artistTypeId, String firstName, String lastName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ArtistType artistType = artistTypeRepository.findById(artistTypeId)
                .orElseThrow(() -> new RuntimeException("Artist type not found"));

        ArtistProfile artistProfile = new ArtistProfile();
        artistProfile.setUser(user);
        artistProfile.setArtistType(artistType);
        artistProfile.setFirstName(firstName);
        artistProfile.setLastName(lastName);

        return artistProfileRepository.save(artistProfile);
    }

    public ArtistProfile updateArtistProfile(Long artistProfileId, ArtistProfile updatedProfile) {
        ArtistProfile existingProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));

        // Note: firstName and lastName are in User entity, not ArtistProfile
        // They should be updated through UserService or a separate method
        if (updatedProfile.getStageName() != null) {
            existingProfile.setStageName(updatedProfile.getStageName());
        }
        if (updatedProfile.getBio() != null) {
            existingProfile.setBio(updatedProfile.getBio());
        }
        if (updatedProfile.getDateOfBirth() != null) {
            existingProfile.setDateOfBirth(updatedProfile.getDateOfBirth());
        }
        if (updatedProfile.getGender() != null) {
            existingProfile.setGender(updatedProfile.getGender());
        }
        if (updatedProfile.getLocation() != null) {
            existingProfile.setLocation(updatedProfile.getLocation());
        }
        // Note: Profile images and portfolio URLs are now handled by Document entity
        // They should be uploaded separately using the document upload API
        if (updatedProfile.getSkills() != null) {
            existingProfile.setSkills(updatedProfile.getSkills());
        }
        if (updatedProfile.getExperienceYears() != null) {
            existingProfile.setExperienceYears(updatedProfile.getExperienceYears());
        }
        if (updatedProfile.getHourlyRate() != null) {
            existingProfile.setHourlyRate(updatedProfile.getHourlyRate());
        }

        return artistProfileRepository.save(existingProfile);
    }

    public void updateBasicProfile(Long userId, UpdateUserProfileDto updateDto) {
        ArtistProfile artistProfile = artistProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));

        User user = artistProfile.getUser();
        if (updateDto.getFirstName() != null) {
            user.setFirstName(updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null) {
            user.setLastName(updateDto.getLastName());
        }
        userRepository.save(user);
        if (updateDto.getBio() != null) {
            artistProfile.setBio(updateDto.getBio());
        }
        if (updateDto.getLocation() != null) {
            artistProfile.setLocation(updateDto.getLocation());
        }
        // Note: Profile images are now handled by Document entity
        // They should be uploaded separately using the document upload API

        artistProfileRepository.save(artistProfile);
    }

    public ArtistProfile requestVerificationBadge(Long artistProfileId) {
        ArtistProfile artistProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));

        artistProfile.setVerificationRequestedAt(java.time.LocalDate.now());
        return artistProfileRepository.save(artistProfile);
    }

    public ArtistProfile approveVerificationBadge(Long artistProfileId) {
        ArtistProfile artistProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));

        artistProfile.setIsVerifiedBadge(true);
        artistProfile.setVerificationApprovedAt(java.time.LocalDate.now());
        return artistProfileRepository.save(artistProfile);
    }

    public ArtistProfile rejectVerificationBadge(Long artistProfileId) {
        ArtistProfile artistProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));

        artistProfile.setVerificationRequestedAt(null);
        return artistProfileRepository.save(artistProfile);
    }

    public void incrementApplications(Long artistProfileId) {
        ArtistProfile artistProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));

        artistProfile.setTotalApplications(artistProfile.getTotalApplications() + 1);
        artistProfileRepository.save(artistProfile);
    }

    public void incrementSuccessfulHires(Long artistProfileId) {
        ArtistProfile artistProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));

        artistProfile.setSuccessfulHires(artistProfile.getSuccessfulHires() + 1);
        artistProfileRepository.save(artistProfile);
    }

    public void deleteArtistProfile(Long artistProfileId) {
        ArtistProfile artistProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));

        artistProfile.setIsActive(false);
        artistProfileRepository.save(artistProfile);
    }

    @Transactional(readOnly = true)
    public List<ArtistProfile> findPendingVerificationRequests() {
        return artistProfileRepository.findPendingVerificationRequests();
    }

    public ArtistProfile save(ArtistProfile artistProfile) {
        return artistProfileRepository.save(artistProfile);
    }

    @Transactional(readOnly = true)
    public List<ArtistProfile> searchArtists(String searchTerm, String location, String skills, 
                                           Long artistTypeId, Integer minExperience, Double maxRate) {
        // This would be implemented with more complex search logic
        // For now, return basic search results
        if (artistTypeId != null) {
            return findActiveArtistsByType(artistTypeId);
        }
        return findActiveArtists();
    }

    /**
     * Save dynamic fields for an artist profile
     * @param artistProfileId The ID of the artist profile
     * @param dynamicFields List of dynamic field data
     */
    @Transactional
    public void saveDynamicFields(Long artistProfileId, List<ArtistProfileFieldDto> dynamicFields) {
        ArtistProfile artistProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));

        // Get existing fields to check for duplicates
        List<ArtistProfileField> existingFields = artistProfileFieldRepository.findByArtistProfileId(artistProfileId);
        
        // Process each dynamic field
        for (ArtistProfileFieldDto fieldDto : dynamicFields) {
            if (fieldDto.getFieldValue() != null && !fieldDto.getFieldValue().trim().isEmpty()) {
                ArtistTypeField artistTypeField = artistTypeFieldRepository.findById(fieldDto.getArtistTypeFieldId())
                        .orElseThrow(() -> new RuntimeException("Artist type field not found with id: " + fieldDto.getArtistTypeFieldId()));

                // Check if field already exists
                Optional<ArtistProfileField> existingField = existingFields.stream()
                        .filter(field -> field.getArtistTypeField().getId().equals(fieldDto.getArtistTypeFieldId()))
                        .findFirst();

                if (existingField.isPresent()) {
                    // Update existing field
                    ArtistProfileField profileField = existingField.get();
                    profileField.setFieldValue(fieldDto.getFieldValue());
                    artistProfileFieldRepository.save(profileField);
                    log.info("Updated dynamic field '{}' for artist profile {}", artistTypeField.getFieldName(), artistProfileId);
                } else {
                    // Create new field
                    ArtistProfileField profileField = new ArtistProfileField();
                    profileField.setArtistProfile(artistProfile);
                    profileField.setArtistTypeField(artistTypeField);
                    profileField.setFieldValue(fieldDto.getFieldValue());
                    
                    // File handling is now managed by Document entity

                    artistProfileFieldRepository.save(profileField);
                    log.info("Saved dynamic field '{}' for artist profile {}", artistTypeField.getFieldName(), artistProfileId);
                }
            }
        }

        // Remove fields that are no longer in the request
        List<Long> requestedFieldIds = dynamicFields.stream()
                .map(ArtistProfileFieldDto::getArtistTypeFieldId)
                .collect(java.util.stream.Collectors.toList());
        
        List<ArtistProfileField> fieldsToDelete = existingFields.stream()
                .filter(field -> !requestedFieldIds.contains(field.getArtistTypeField().getId()))
                .collect(java.util.stream.Collectors.toList());
        
        if (!fieldsToDelete.isEmpty()) {
            artistProfileFieldRepository.deleteAll(fieldsToDelete);
            log.info("Deleted {} unused dynamic fields for artist profile {}", fieldsToDelete.size(), artistProfileId);
        }
    }

    /**
     * Get dynamic fields for an artist profile
     * @param artistProfileId The ID of the artist profile
     * @return List of dynamic field data
     */
    @Transactional(readOnly = true)
    public List<ArtistProfileFieldDto> getDynamicFields(Long artistProfileId) {
        List<ArtistProfileField> fields = artistProfileFieldRepository.findByArtistProfileId(artistProfileId);
        return fields.stream()
                .map(this::convertToFieldDto)
                .collect(java.util.stream.Collectors.toList());
    }

    private ArtistProfileFieldDto convertToFieldDto(ArtistProfileField field) {
        ArtistProfileFieldDto dto = new ArtistProfileFieldDto();
        dto.setId(field.getId());
        dto.setArtistTypeFieldId(field.getArtistTypeField().getId());
        dto.setFieldName(field.getArtistTypeField().getFieldName());
        dto.setDisplayName(field.getArtistTypeField().getDisplayName());
        dto.setFieldValue(field.getFieldValue());
        // File handling is now managed by Document entity
        return dto;
    }
}

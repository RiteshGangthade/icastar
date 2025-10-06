package com.icastar.platform.service;

import com.icastar.platform.dto.artist.ArtistProfileCompleteDto;
import com.icastar.platform.dto.artist.ArtistProfileCompleteDto.DocumentDto;
import com.icastar.platform.entity.ArtistProfile;
import com.icastar.platform.entity.Document;
import com.icastar.platform.entity.User;
import com.icastar.platform.repository.ArtistProfileRepository;
import com.icastar.platform.repository.DocumentRepository;
import com.icastar.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtistProfileService {

    private final ArtistProfileRepository artistProfileRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;

    /**
     * Get complete artist profile by user ID
     */
    @Transactional(readOnly = true)
    public Optional<ArtistProfileCompleteDto> getCompleteProfileByUserId(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            ArtistProfile artistProfile = artistProfileRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));
            
            List<Document> documents = documentRepository.findByUserId(userId);
            
            return Optional.of(mapToCompleteDto(user, artistProfile, documents));
        } catch (Exception e) {
            log.error("Error getting complete profile for user ID: {}", userId, e);
            return Optional.empty();
        }
    }

    /**
     * Get complete artist profile by artist profile ID
     */
    @Transactional(readOnly = true)
    public Optional<ArtistProfileCompleteDto> getCompleteProfileById(Long artistProfileId) {
        try {
            ArtistProfile artistProfile = artistProfileRepository.findById(artistProfileId)
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));
            
            User user = artistProfile.getUser();
            List<Document> documents = documentRepository.findByUserId(user.getId());
            
            return Optional.of(mapToCompleteDto(user, artistProfile, documents));
        } catch (Exception e) {
            log.error("Error getting complete profile for artist profile ID: {}", artistProfileId, e);
            return Optional.empty();
        }
    }

    /**
     * Get all artist profiles with pagination
     */
    @Transactional(readOnly = true)
    public Page<ArtistProfileCompleteDto> getAllCompleteProfiles(Pageable pageable) {
        try {
            Page<ArtistProfile> artistProfiles = artistProfileRepository.findAll(pageable);
            
            return artistProfiles.map(artistProfile -> {
                User user = artistProfile.getUser();
                List<Document> documents = documentRepository.findByUserId(user.getId());
                return mapToCompleteDto(user, artistProfile, documents);
            });
        } catch (Exception e) {
            log.error("Error getting all complete profiles", e);
            throw new RuntimeException("Failed to get artist profiles: " + e.getMessage());
        }
    }

    /**
     * Get artist profiles by artist type
     */
    @Transactional(readOnly = true)
    public List<ArtistProfileCompleteDto> getProfilesByArtistType(String artistTypeName) {
        try {
            List<ArtistProfile> artistProfiles = artistProfileRepository.findByArtistTypeName(artistTypeName);
            
            return artistProfiles.stream().map(artistProfile -> {
                User user = artistProfile.getUser();
                List<Document> documents = documentRepository.findByUserId(user.getId());
                return mapToCompleteDto(user, artistProfile, documents);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting profiles by artist type: {}", artistTypeName, e);
            throw new RuntimeException("Failed to get artist profiles: " + e.getMessage());
        }
    }

    /**
     * Search artist profiles
     */
    @Transactional(readOnly = true)
    public List<ArtistProfileCompleteDto> searchProfiles(String searchTerm) {
        try {
            List<ArtistProfile> artistProfiles = artistProfileRepository.findBySearchTerm(searchTerm);
            
            return artistProfiles.stream().map(artistProfile -> {
                User user = artistProfile.getUser();
                List<Document> documents = documentRepository.findByUserId(user.getId());
                return mapToCompleteDto(user, artistProfile, documents);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching profiles with term: {}", searchTerm, e);
            throw new RuntimeException("Failed to search artist profiles: " + e.getMessage());
        }
    }

    /**
     * Update artist profile basic information
     */
    @Transactional
    public ArtistProfileCompleteDto updateProfile(Long userId, ArtistProfileCompleteDto updateDto) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            ArtistProfile artistProfile = artistProfileRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));
            
            // Update user fields
            if (updateDto.getFirstName() != null) {
                user.setFirstName(updateDto.getFirstName());
            }
            if (updateDto.getLastName() != null) {
                user.setLastName(updateDto.getLastName());
            }
            if (updateDto.getPhone() != null) {
                user.setMobile(updateDto.getPhone()); // User entity has 'mobile' field, not 'phone'
            }
            // Note: User entity doesn't have 'city' field, it's in ArtistProfile
            
            // Update artist profile fields
            if (updateDto.getStageName() != null) {
                artistProfile.setStageName(updateDto.getStageName());
            }
            if (updateDto.getBio() != null) {
                artistProfile.setBio(updateDto.getBio());
            }
            if (updateDto.getDateOfBirth() != null) {
                artistProfile.setDateOfBirth(updateDto.getDateOfBirth());
            }
            if (updateDto.getGender() != null) {
                artistProfile.setGender(updateDto.getGender());
            }
            if (updateDto.getLocation() != null) {
                artistProfile.setLocation(updateDto.getLocation());
            }
            if (updateDto.getMaritalStatus() != null) {
                artistProfile.setMaritalStatus(updateDto.getMaritalStatus());
            }
            if (updateDto.getLanguagesSpoken() != null) {
                artistProfile.setLanguagesSpoken(updateDto.getLanguagesSpoken());
            }
            if (updateDto.getComfortableAreas() != null) {
                artistProfile.setComfortableAreas(updateDto.getComfortableAreas());
            }
            if (updateDto.getProjectsWorked() != null) {
                artistProfile.setProjectsWorked(updateDto.getProjectsWorked());
            }
            if (updateDto.getSkills() != null) {
                artistProfile.setSkills(updateDto.getSkills());
            }
            if (updateDto.getExperienceYears() != null) {
                artistProfile.setExperienceYears(updateDto.getExperienceYears());
            }
            if (updateDto.getWeight() != null) {
                artistProfile.setWeight(updateDto.getWeight());
            }
            if (updateDto.getHeight() != null) {
                artistProfile.setHeight(updateDto.getHeight());
            }
            if (updateDto.getHairColor() != null) {
                artistProfile.setHairColor(updateDto.getHairColor());
            }
            if (updateDto.getHairLength() != null) {
                artistProfile.setHairLength(updateDto.getHairLength());
            }
            if (updateDto.getHasTattoo() != null) {
                artistProfile.setHasTattoo(updateDto.getHasTattoo());
            }
            if (updateDto.getHasMole() != null) {
                artistProfile.setHasMole(updateDto.getHasMole());
            }
            if (updateDto.getShoeSize() != null) {
                artistProfile.setShoeSize(updateDto.getShoeSize());
            }
            if (updateDto.getTravelCities() != null) {
                artistProfile.setTravelCities(updateDto.getTravelCities());
            }
            if (updateDto.getHourlyRate() != null) {
                artistProfile.setHourlyRate(updateDto.getHourlyRate());
            }
            
            // Save updates
            userRepository.save(user);
            artistProfileRepository.save(artistProfile);
            
            // Get updated documents
            List<Document> documents = documentRepository.findByUserId(userId);
            
            return mapToCompleteDto(user, artistProfile, documents);
        } catch (Exception e) {
            log.error("Error updating profile for user ID: {}", userId, e);
            throw new RuntimeException("Failed to update profile: " + e.getMessage());
        }
    }

    /**
     * Delete artist profile
     */
    @Transactional
    public void deleteProfile(Long userId) {
        try {
            ArtistProfile artistProfile = artistProfileRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));
            
            // Delete documents first
            List<Document> documents = documentRepository.findByUserId(userId);
            documentRepository.deleteAll(documents);
            
            // Delete artist profile
            artistProfileRepository.delete(artistProfile);
            
            log.info("Successfully deleted artist profile for user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error deleting profile for user ID: {}", userId, e);
            throw new RuntimeException("Failed to delete profile: " + e.getMessage());
        }
    }

    /**
     * Map entities to complete DTO
     */
    private ArtistProfileCompleteDto mapToCompleteDto(User user, ArtistProfile artistProfile, List<Document> documents) {
        ArtistProfileCompleteDto dto = new ArtistProfileCompleteDto();
        
        // User fields
        dto.setUserId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getMobile()); // User entity has 'mobile' field, not 'phone'
        dto.setCity(artistProfile.getLocation()); // City is stored in ArtistProfile.location
        dto.setIsActive(user.getStatus() == com.icastar.platform.entity.User.UserStatus.ACTIVE);
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        // Artist Profile fields
        dto.setArtistProfileId(artistProfile.getId());
        dto.setArtistTypeId(artistProfile.getArtistType().getId());
        dto.setArtistTypeName(artistProfile.getArtistType().getName());
        dto.setStageName(artistProfile.getStageName());
        dto.setBio(artistProfile.getBio());
        dto.setDateOfBirth(artistProfile.getDateOfBirth());
        dto.setGender(artistProfile.getGender());
        dto.setLocation(artistProfile.getLocation());
        dto.setMaritalStatus(artistProfile.getMaritalStatus());
        dto.setLanguagesSpoken(artistProfile.getLanguagesSpoken());
        dto.setComfortableAreas(artistProfile.getComfortableAreas());
        dto.setProjectsWorked(artistProfile.getProjectsWorked());
        dto.setSkills(artistProfile.getSkills());
        dto.setExperienceYears(artistProfile.getExperienceYears());
        dto.setWeight(artistProfile.getWeight());
        dto.setHeight(artistProfile.getHeight());
        dto.setHairColor(artistProfile.getHairColor());
        dto.setHairLength(artistProfile.getHairLength());
        dto.setHasTattoo(artistProfile.getHasTattoo());
        dto.setHasMole(artistProfile.getHasMole());
        dto.setShoeSize(artistProfile.getShoeSize());
        dto.setTravelCities(artistProfile.getTravelCities());
        dto.setHourlyRate(artistProfile.getHourlyRate());
        dto.setIsVerifiedBadge(artistProfile.getIsVerifiedBadge());
        dto.setVerificationRequestedAt(artistProfile.getVerificationRequestedAt());
        dto.setVerificationApprovedAt(artistProfile.getVerificationApprovedAt());
        dto.setTotalApplications(artistProfile.getTotalApplications());
        dto.setSuccessfulHires(artistProfile.getSuccessfulHires());
        
        // Documents
        dto.setDocuments(documents.stream().map(this::mapToDocumentDto).collect(Collectors.toList()));
        
        return dto;
    }

    /**
     * Map Document entity to DocumentDto
     */
    private DocumentDto mapToDocumentDto(Document document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId());
        dto.setDocumentType(document.getDocumentType());
        dto.setFileName(document.getFileName());
        dto.setFileUrl(document.getFileUrl());
        dto.setFileSize(document.getFileSize());
        dto.setMimeType(document.getMimeType());
        dto.setUploadedAt(document.getUploadedAt());
        dto.setIsVerified(document.getIsVerified());
        dto.setVerifiedAt(document.getVerifiedAt());
        dto.setVerifiedBy(document.getVerifiedBy());
        dto.setVerificationNotes(document.getVerificationNotes());
        return dto;
    }
}

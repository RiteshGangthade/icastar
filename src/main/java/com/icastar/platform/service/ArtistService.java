package com.icastar.platform.service;

import com.icastar.platform.dto.user.UpdateUserProfileDto;
import com.icastar.platform.entity.ArtistProfile;
import com.icastar.platform.entity.ArtistType;
import com.icastar.platform.entity.User;
import com.icastar.platform.repository.ArtistProfileRepository;
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
        artistProfile.setIsActive(true);

        return artistProfileRepository.save(artistProfile);
    }

    public ArtistProfile updateArtistProfile(Long artistProfileId, ArtistProfile updatedProfile) {
        ArtistProfile existingProfile = artistProfileRepository.findById(artistProfileId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));

        // Update fields
        if (updatedProfile.getFirstName() != null) {
            existingProfile.setFirstName(updatedProfile.getFirstName());
        }
        if (updatedProfile.getLastName() != null) {
            existingProfile.setLastName(updatedProfile.getLastName());
        }
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
        if (updatedProfile.getProfileImageUrl() != null) {
            existingProfile.setProfileImageUrl(updatedProfile.getProfileImageUrl());
        }
        if (updatedProfile.getPortfolioUrls() != null) {
            existingProfile.setPortfolioUrls(updatedProfile.getPortfolioUrls());
        }
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

        if (updateDto.getFirstName() != null) {
            artistProfile.setFirstName(updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null) {
            artistProfile.setLastName(updateDto.getLastName());
        }
        if (updateDto.getBio() != null) {
            artistProfile.setBio(updateDto.getBio());
        }
        if (updateDto.getLocation() != null) {
            artistProfile.setLocation(updateDto.getLocation());
        }
        if (updateDto.getProfileImageUrl() != null) {
            artistProfile.setProfileImageUrl(updateDto.getProfileImageUrl());
        }

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
}

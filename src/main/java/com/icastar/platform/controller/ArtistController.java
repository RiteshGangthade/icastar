package com.icastar.platform.controller;

import com.icastar.platform.dto.ArtistProfileFieldDto;
import com.icastar.platform.dto.artist.CreateArtistProfileDto;
import com.icastar.platform.entity.ArtistProfile;
import com.icastar.platform.entity.User;
import com.icastar.platform.service.ArtistService;
import com.icastar.platform.service.ArtistTypeService;
import com.icastar.platform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Artists", description = "Artist profile management and operations")
@SecurityRequirement(name = "bearerAuth")
public class ArtistController {

    private final ArtistService artistService;
    private final ArtistTypeService artistTypeService;
    private final UserService userService;

    @PostMapping("/profile")
    public ResponseEntity<Map<String, Object>> createArtistProfile(@Valid @RequestBody CreateArtistProfileDto createDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Check if artist profile already exists
            if (artistService.findByUserId(user.getId()).isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Artist profile already exists");
                return ResponseEntity.badRequest().body(response);
            }

            ArtistProfile artistProfile = artistService.createArtistProfile(
                    user.getId(), 
                    createDto.getArtistTypeId(), 
                    createDto.getFirstName(), 
                    createDto.getLastName()
            );

            // Update additional fields
            if (createDto.getStageName() != null) {
                artistProfile.setStageName(createDto.getStageName());
            }
            if (createDto.getBio() != null) {
                artistProfile.setBio(createDto.getBio());
            }
            if (createDto.getDateOfBirth() != null) {
                artistProfile.setDateOfBirth(createDto.getDateOfBirth());
            }
            if (createDto.getGender() != null) {
                artistProfile.setGender(createDto.getGender());
            }
            if (createDto.getLocation() != null) {
                artistProfile.setLocation(createDto.getLocation());
            }
            if (createDto.getProfileImageUrl() != null) {
                artistProfile.setProfileImageUrl(createDto.getProfileImageUrl());
            }
            if (createDto.getPortfolioUrls() != null) {
                artistProfile.setPortfolioUrls(String.join(",", createDto.getPortfolioUrls()));
            }
            if (createDto.getSkills() != null) {
                artistProfile.setSkills(String.join(",", createDto.getSkills()));
            }
            if (createDto.getExperienceYears() != null) {
                artistProfile.setExperienceYears(createDto.getExperienceYears());
            }
            if (createDto.getHourlyRate() != null) {
                artistProfile.setHourlyRate(createDto.getHourlyRate());
            }

            artistService.save(artistProfile);

            // Handle dynamic fields if provided
            if (createDto.getDynamicFields() != null && !createDto.getDynamicFields().isEmpty()) {
                artistService.saveDynamicFields(artistProfile.getId(), createDto.getDynamicFields());
            }

            // Get dynamic fields for the response
            List<ArtistProfileFieldDto> dynamicFields = artistService.getDynamicFields(artistProfile.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Artist profile created successfully");
            response.put("data", artistProfile);
            response.put("dynamicFields", dynamicFields);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating artist profile", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to create artist profile");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getCurrentArtistProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ArtistProfile artistProfile = artistService.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            // Get dynamic fields for the response
            List<ArtistProfileFieldDto> dynamicFields = artistService.getDynamicFields(artistProfile.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", artistProfile);
            response.put("dynamicFields", dynamicFields);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting artist profile", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get artist profile");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<Map<String, Object>> getArtistProfile(@PathVariable Long id) {
        try {
            ArtistProfile artistProfile = artistService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            // Get dynamic fields for the response
            List<ArtistProfileFieldDto> dynamicFields = artistService.getDynamicFields(artistProfile.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", artistProfile);
            response.put("dynamicFields", dynamicFields);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting artist profile", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get artist profile");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateArtistProfile(@Valid @RequestBody CreateArtistProfileDto updateDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ArtistProfile existingProfile = artistService.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            // Create updated profile object
            ArtistProfile updatedProfile = new ArtistProfile();
            updatedProfile.setFirstName(updateDto.getFirstName());
            updatedProfile.setLastName(updateDto.getLastName());
            updatedProfile.setStageName(updateDto.getStageName());
            updatedProfile.setBio(updateDto.getBio());
            updatedProfile.setDateOfBirth(updateDto.getDateOfBirth());
            updatedProfile.setGender(updateDto.getGender());
            updatedProfile.setLocation(updateDto.getLocation());
            updatedProfile.setProfileImageUrl(updateDto.getProfileImageUrl());
            updatedProfile.setPortfolioUrls(updateDto.getPortfolioUrls() != null ? String.join(",", updateDto.getPortfolioUrls()) : null);
            updatedProfile.setSkills(updateDto.getSkills() != null ? String.join(",", updateDto.getSkills()) : null);
            updatedProfile.setExperienceYears(updateDto.getExperienceYears());
            updatedProfile.setHourlyRate(updateDto.getHourlyRate());

            ArtistProfile savedProfile = artistService.updateArtistProfile(existingProfile.getId(), updatedProfile);

            // Handle dynamic fields if provided
            if (updateDto.getDynamicFields() != null && !updateDto.getDynamicFields().isEmpty()) {
                artistService.saveDynamicFields(existingProfile.getId(), updateDto.getDynamicFields());
            }

            List<ArtistProfileFieldDto> dynamicFields = artistService.getDynamicFields(existingProfile.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Artist profile updated successfully");
            response.put("data", savedProfile);
            response.put("dynamicFields", dynamicFields);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating artist profile", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update artist profile");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/profile/verify")
    public ResponseEntity<Map<String, Object>> requestVerificationBadge() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ArtistProfile artistProfile = artistService.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            artistService.requestVerificationBadge(artistProfile.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Verification request submitted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error requesting verification badge", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to request verification badge");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchArtists(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) Long artistTypeId,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Double maxRate,
            Pageable pageable) {
        try {
            List<ArtistProfile> artists = artistService.searchArtists(
                    searchTerm, location, skills, artistTypeId, minExperience, maxRate);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", artists);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching artists", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to search artists");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/type/{artistTypeId}")
    public ResponseEntity<Map<String, Object>> getArtistsByType(@PathVariable Long artistTypeId) {
        try {
            List<ArtistProfile> artists = artistService.findActiveArtistsByType(artistTypeId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", artists);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting artists by type", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get artists by type");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/verified")
    public ResponseEntity<Map<String, Object>> getVerifiedArtists() {
        try {
            List<ArtistProfile> artists = artistService.findActiveVerifiedArtists();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", artists);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting verified artists", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get verified artists");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

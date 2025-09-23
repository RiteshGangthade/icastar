package com.icastar.platform.dto.artist;

import com.icastar.platform.dto.ArtistProfileFieldDto;
import com.icastar.platform.entity.ArtistProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateArtistProfileDto {
    
    @NotNull(message = "Artist type ID is required")
    private Long artistTypeId;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @Size(max = 100, message = "Stage name must not exceed 100 characters")
    private String stageName;
    
    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;
    
    private LocalDate dateOfBirth;
    
    private ArtistProfile.Gender gender;
    
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;
    
    private String profileImageUrl;
    
    private List<String> portfolioUrls;
    
    private List<String> skills;
    
    private Integer experienceYears;
    
    private Double hourlyRate;
    
    private List<ArtistProfileFieldDto> dynamicFields;
}

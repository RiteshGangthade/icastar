package com.icastar.platform.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserProfileDto {
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Invalid mobile number format")
    private String mobile;
    
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;
    
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;
    
    private String profileImageUrl;
}

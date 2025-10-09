package com.icastar.platform.controller;

import com.icastar.platform.dto.job.BookmarkedJobDto;
import com.icastar.platform.dto.job.CreateBookmarkDto;
import com.icastar.platform.entity.BookmarkedJob;
import com.icastar.platform.entity.ArtistProfile;
import com.icastar.platform.entity.User;
import com.icastar.platform.service.BookmarkedJobService;
import com.icastar.platform.service.ArtistService;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bookmarked Jobs Management", description = "APIs for managing bookmarked jobs")
public class BookmarkedJobController {

    private final BookmarkedJobService bookmarkedJobService;
    private final ArtistService artistService;
    private final UserService userService;

    @Operation(summary = "Bookmark a job", description = "Add a job to bookmarks")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{jobId}")
    public ResponseEntity<Map<String, Object>> bookmarkJob(
            @PathVariable Long jobId,
            @Valid @RequestBody(required = false) CreateBookmarkDto createDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ArtistProfile artistProfile = artistService.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            CreateBookmarkDto bookmarkDto = createDto != null ? createDto : new CreateBookmarkDto();
            BookmarkedJob bookmark = bookmarkedJobService.bookmarkJob(artistProfile.getId(), jobId, bookmarkDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Job bookmarked successfully");
            response.put("data", bookmark);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error bookmarking job", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to bookmark job");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get my bookmarks", description = "Get all bookmarked jobs for the current user")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getMyBookmarks(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Show only active jobs") @RequestParam(defaultValue = "true") boolean activeOnly) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ArtistProfile artistProfile = artistService.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "bookmarkedAt"));
            Page<BookmarkedJob> bookmarks;

            if (activeOnly) {
                bookmarks = bookmarkedJobService.findActiveBookmarksByArtist(artistProfile, pageable);
            } else {
                bookmarks = bookmarkedJobService.findByArtist(artistProfile, pageable);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", bookmarks.getContent());
            response.put("totalElements", bookmarks.getTotalElements());
            response.put("totalPages", bookmarks.getTotalPages());
            response.put("currentPage", bookmarks.getNumber());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving bookmarks", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve bookmarks");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get bookmarks with notes", description = "Get bookmarked jobs that have personal notes")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/with-notes")
    public ResponseEntity<Map<String, Object>> getBookmarksWithNotes() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ArtistProfile artistProfile = artistService.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            List<BookmarkedJob> bookmarks = bookmarkedJobService.findBookmarksWithNotes(artistProfile);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", bookmarks);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving bookmarks with notes", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve bookmarks with notes");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Update bookmark", description = "Update notes for a bookmarked job")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{bookmarkId}")
    public ResponseEntity<Map<String, Object>> updateBookmark(
            @PathVariable Long bookmarkId,
            @Valid @RequestBody CreateBookmarkDto updateDto) {
        try {
            BookmarkedJob bookmark = bookmarkedJobService.updateBookmark(bookmarkId, updateDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Bookmark updated successfully");
            response.put("data", bookmark);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating bookmark", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update bookmark");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Remove bookmark", description = "Remove a job from bookmarks")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Map<String, Object>> removeBookmark(@PathVariable Long bookmarkId) {
        try {
            bookmarkedJobService.removeBookmark(bookmarkId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Bookmark removed successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error removing bookmark", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to remove bookmark");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Remove bookmark by job", description = "Remove a job from bookmarks by job ID")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/job/{jobId}")
    public ResponseEntity<Map<String, Object>> removeBookmarkByJob(@PathVariable Long jobId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ArtistProfile artistProfile = artistService.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            bookmarkedJobService.removeBookmarkByJob(artistProfile.getId(), jobId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Bookmark removed successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error removing bookmark by job", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to remove bookmark");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Check if job is bookmarked", description = "Check if a job is bookmarked by the current user")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/check/{jobId}")
    public ResponseEntity<Map<String, Object>> checkBookmarkStatus(@PathVariable Long jobId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ArtistProfile artistProfile = artistService.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            boolean isBookmarked = bookmarkedJobService.isJobBookmarked(artistProfile.getId(), jobId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("isBookmarked", isBookmarked);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking bookmark status", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to check bookmark status");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get bookmark statistics", description = "Get statistics about user's bookmarks")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getBookmarkStats() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ArtistProfile artistProfile = artistService.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            Long totalBookmarks = bookmarkedJobService.countBookmarksByArtist(artistProfile);
            List<BookmarkedJob> bookmarksWithNotes = bookmarkedJobService.findBookmarksWithNotes(artistProfile);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", Map.of(
                    "totalBookmarks", totalBookmarks,
                    "bookmarksWithNotes", bookmarksWithNotes.size()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving bookmark stats", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve bookmark statistics");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

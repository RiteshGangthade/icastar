package com.icastar.platform.config;

import com.icastar.platform.entity.ArtistType;
import com.icastar.platform.entity.ArtistTypeField;
import com.icastar.platform.entity.FieldType;
import com.icastar.platform.repository.ArtistTypeRepository;
import com.icastar.platform.repository.ArtistTypeFieldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

// @Component
@RequiredArgsConstructor
@Slf4j
public class ArtistTypeDataInitializer implements CommandLineRunner {

    private final ArtistTypeRepository artistTypeRepository;
    private final ArtistTypeFieldRepository artistTypeFieldRepository;

    @Override
    public void run(String... args) throws Exception {
        if (artistTypeRepository.count() == 0) {
            log.info("Initializing artist types and fields...");
            initializeArtistTypes();
            log.info("Artist types and fields initialized successfully!");
        }
    }

    private void initializeArtistTypes() {
        // Actor
        ArtistType actor = createArtistType("ACTOR", "Actor", "Film, TV, and Theater Actors", 1);
        actor = artistTypeRepository.save(actor);
        createActorFields(actor);

        // Dancer
        ArtistType dancer = createArtistType("DANCER", "Dancer", "Professional Dancers", 2);
        dancer = artistTypeRepository.save(dancer);
        createDancerFields(dancer);

        // Singer
        ArtistType singer = createArtistType("SINGER", "Singer", "Vocal Artists and Musicians", 3);
        singer = artistTypeRepository.save(singer);
        createSingerFields(singer);

        // Director
        ArtistType director = createArtistType("DIRECTOR", "Director", "Film, TV, and Theater Directors", 4);
        director = artistTypeRepository.save(director);
        createDirectorFields(director);

        // Writer
        ArtistType writer = createArtistType("WRITER", "Writer", "Script Writers, Content Writers", 5);
        writer = artistTypeRepository.save(writer);
        createWriterFields(writer);

        // DJ/RJ
        ArtistType dj = createArtistType("DJ_RJ", "DJ/RJ", "Disc Jockeys and Radio Jockeys", 6);
        dj = artistTypeRepository.save(dj);
        createDjFields(dj);

        // Band
        ArtistType band = createArtistType("BAND", "Band", "Musical Bands and Groups", 7);
        band = artistTypeRepository.save(band);
        createBandFields(band);

        // Model
        ArtistType model = createArtistType("MODEL", "Model", "Fashion and Commercial Models", 8);
        model = artistTypeRepository.save(model);
        createModelFields(model);

        // Photographer
        ArtistType photographer = createArtistType("PHOTOGRAPHER", "Photographer", "Professional Photographers", 9);
        photographer = artistTypeRepository.save(photographer);
        createPhotographerFields(photographer);

        // Videographer
        ArtistType videographer = createArtistType("VIDEOGRAPHER", "Videographer", "Video Production Specialists", 10);
        videographer = artistTypeRepository.save(videographer);
        createVideographerFields(videographer);
    }

    private ArtistType createArtistType(String name, String displayName, String description, int sortOrder) {
        ArtistType artistType = new ArtistType();
        artistType.setName(name);
        artistType.setDisplayName(displayName);
        artistType.setDescription(description);
        artistType.setSortOrder(sortOrder);
        artistType.setIsActive(true);
        return artistType;
    }

    private void createActorFields(ArtistType actor) {
        List<ArtistTypeField> fields = Arrays.asList(
            createField(actor, "height", "Height", FieldType.TEXT, true, true, 1, "cm", "Height in centimeters"),
            createField(actor, "weight", "Weight", FieldType.TEXT, false, true, 2, "kg", "Weight in kilograms"),
            createField(actor, "body_type", "Body Type", FieldType.SELECT, false, true, 3, null, "Body type category"),
            createField(actor, "hair_color", "Hair Color", FieldType.SELECT, false, true, 4, null, "Natural hair color"),
            createField(actor, "eye_color", "Eye Color", FieldType.SELECT, false, true, 5, null, "Eye color"),
            createField(actor, "skin_tone", "Skin Tone", FieldType.SELECT, false, true, 6, null, "Skin tone category"),
            createField(actor, "languages", "Languages Known", FieldType.MULTI_SELECT, true, true, 7, null, "Languages you can perform in"),
            createField(actor, "acting_experience", "Acting Experience", FieldType.SELECT, true, true, 8, null, "Years of acting experience"),
            createField(actor, "special_skills", "Special Skills", FieldType.MULTI_SELECT, false, true, 9, null, "Special acting skills (dancing, singing, etc.)"),
            createField(actor, "demo_reel", "Demo Reel", FieldType.URL, false, false, 10, "https://", "Link to your demo reel"),
            createField(actor, "headshots", "Headshots", FieldType.FILE, true, false, 11, null, "Professional headshot photos"),
            createField(actor, "resume", "Acting Resume", FieldType.FILE, false, false, 12, null, "Your acting resume/CV")
        );
        artistTypeFieldRepository.saveAll(fields);
    }

    private void createDancerFields(ArtistType dancer) {
        List<ArtistTypeField> fields = Arrays.asList(
            createField(dancer, "dance_styles", "Dance Styles", FieldType.MULTI_SELECT, true, true, 1, null, "Dance styles you specialize in"),
            createField(dancer, "training_background", "Training Background", FieldType.TEXTAREA, true, true, 2, null, "Your dance training and education"),
            createField(dancer, "performance_experience", "Performance Experience", FieldType.SELECT, true, true, 3, null, "Years of performance experience"),
            createField(dancer, "choreography_skills", "Choreography Skills", FieldType.BOOLEAN, false, true, 4, null, "Can you create choreography?"),
            createField(dancer, "teaching_experience", "Teaching Experience", FieldType.BOOLEAN, false, true, 5, null, "Do you have teaching experience?"),
            createField(dancer, "performance_videos", "Performance Videos", FieldType.FILE, true, false, 6, null, "Videos of your performances"),
            createField(dancer, "costume_availability", "Costume Availability", FieldType.BOOLEAN, false, false, 7, null, "Do you have your own costumes?")
        );
        artistTypeFieldRepository.saveAll(fields);
    }

    private void createSingerFields(ArtistType singer) {
        List<ArtistTypeField> fields = Arrays.asList(
            createField(singer, "vocal_range", "Vocal Range", FieldType.SELECT, true, true, 1, null, "Your vocal range"),
            createField(singer, "music_genres", "Music Genres", FieldType.MULTI_SELECT, true, true, 2, null, "Genres you can perform"),
            createField(singer, "instruments", "Instruments", FieldType.MULTI_SELECT, false, true, 3, null, "Instruments you can play"),
            createField(singer, "recording_experience", "Recording Experience", FieldType.BOOLEAN, false, true, 4, null, "Do you have recording experience?"),
            createField(singer, "live_performance", "Live Performance", FieldType.BOOLEAN, true, true, 5, null, "Can you perform live?"),
            createField(singer, "demo_tracks", "Demo Tracks", FieldType.FILE, true, false, 6, null, "Your demo recordings"),
            createField(singer, "original_compositions", "Original Compositions", FieldType.BOOLEAN, false, true, 7, null, "Do you write original music?")
        );
        artistTypeFieldRepository.saveAll(fields);
    }

    private void createDirectorFields(ArtistType director) {
        List<ArtistTypeField> fields = Arrays.asList(
            createField(director, "directing_experience", "Directing Experience", FieldType.SELECT, true, true, 1, null, "Years of directing experience"),
            createField(director, "project_types", "Project Types", FieldType.MULTI_SELECT, true, true, 2, null, "Types of projects you direct"),
            createField(director, "equipment_owned", "Equipment Owned", FieldType.MULTI_SELECT, false, true, 3, null, "Production equipment you own"),
            createField(director, "team_size", "Team Size", FieldType.SELECT, false, true, 4, null, "Size of teams you can manage"),
            createField(director, "portfolio", "Portfolio", FieldType.URL, true, false, 5, "https://", "Link to your directing portfolio"),
            createField(director, "awards", "Awards", FieldType.TEXTAREA, false, false, 6, null, "Awards and recognitions received")
        );
        artistTypeFieldRepository.saveAll(fields);
    }

    private void createWriterFields(ArtistType writer) {
        List<ArtistTypeField> fields = Arrays.asList(
            createField(writer, "writing_experience", "Writing Experience", FieldType.SELECT, true, true, 1, null, "Years of writing experience"),
            createField(writer, "writing_types", "Writing Types", FieldType.MULTI_SELECT, true, true, 2, null, "Types of content you write"),
            createField(writer, "languages", "Languages", FieldType.MULTI_SELECT, true, true, 3, null, "Languages you write in"),
            createField(writer, "published_works", "Published Works", FieldType.TEXTAREA, false, false, 4, null, "List of your published works"),
            createField(writer, "writing_samples", "Writing Samples", FieldType.FILE, true, false, 5, null, "Samples of your writing work")
        );
        artistTypeFieldRepository.saveAll(fields);
    }

    private void createDjFields(ArtistType dj) {
        List<ArtistTypeField> fields = Arrays.asList(
            createField(dj, "music_genres", "Music Genres", FieldType.MULTI_SELECT, true, true, 1, null, "Genres you can DJ"),
            createField(dj, "equipment_owned", "Equipment Owned", FieldType.MULTI_SELECT, false, true, 2, null, "DJ equipment you own"),
            createField(dj, "venue_experience", "Venue Experience", FieldType.MULTI_SELECT, true, true, 3, null, "Types of venues you've performed at"),
            createField(dj, "mixing_skills", "Mixing Skills", FieldType.SELECT, true, true, 4, null, "Your mixing skill level"),
            createField(dj, "demo_mix", "Demo Mix", FieldType.FILE, true, false, 5, null, "Your demo mix recording")
        );
        artistTypeFieldRepository.saveAll(fields);
    }

    private void createBandFields(ArtistType band) {
        List<ArtistTypeField> fields = Arrays.asList(
            createField(band, "band_size", "Band Size", FieldType.SELECT, true, true, 1, null, "Number of band members"),
            createField(band, "music_genres", "Music Genres", FieldType.MULTI_SELECT, true, true, 2, null, "Genres your band performs"),
            createField(band, "instruments", "Instruments", FieldType.MULTI_SELECT, true, true, 3, null, "Instruments in your band"),
            createField(band, "performance_experience", "Performance Experience", FieldType.SELECT, true, true, 4, null, "Years of band performance experience"),
            createField(band, "original_songs", "Original Songs", FieldType.BOOLEAN, false, true, 5, null, "Do you perform original compositions?"),
            createField(band, "demo_tracks", "Demo Tracks", FieldType.FILE, true, false, 6, null, "Your band's demo recordings"),
            createField(band, "equipment_owned", "Equipment Owned", FieldType.MULTI_SELECT, false, true, 7, null, "Sound equipment your band owns")
        );
        artistTypeFieldRepository.saveAll(fields);
    }

    private void createModelFields(ArtistType model) {
        List<ArtistTypeField> fields = Arrays.asList(
            createField(model, "height", "Height", FieldType.TEXT, true, true, 1, "cm", "Height in centimeters"),
            createField(model, "weight", "Weight", FieldType.TEXT, false, true, 2, "kg", "Weight in kilograms"),
            createField(model, "body_measurements", "Body Measurements", FieldType.TEXT, false, true, 3, null, "Bust-Waist-Hip measurements"),
            createField(model, "hair_color", "Hair Color", FieldType.SELECT, false, true, 4, null, "Current hair color"),
            createField(model, "eye_color", "Eye Color", FieldType.SELECT, false, true, 5, null, "Eye color"),
            createField(model, "modeling_types", "Modeling Types", FieldType.MULTI_SELECT, true, true, 6, null, "Types of modeling you do"),
            createField(model, "portfolio", "Portfolio", FieldType.URL, true, false, 7, "https://", "Link to your modeling portfolio"),
            createField(model, "comp_cards", "Comp Cards", FieldType.FILE, false, false, 8, null, "Your composite cards")
        );
        artistTypeFieldRepository.saveAll(fields);
    }

    private void createPhotographerFields(ArtistType photographer) {
        List<ArtistTypeField> fields = Arrays.asList(
            createField(photographer, "photography_types", "Photography Types", FieldType.MULTI_SELECT, true, true, 1, null, "Types of photography you specialize in"),
            createField(photographer, "equipment_owned", "Equipment Owned", FieldType.MULTI_SELECT, false, true, 2, null, "Photography equipment you own"),
            createField(photographer, "editing_software", "Editing Software", FieldType.MULTI_SELECT, false, true, 3, null, "Photo editing software you use"),
            createField(photographer, "portfolio", "Portfolio", FieldType.URL, true, false, 4, "https://", "Link to your photography portfolio"),
            createField(photographer, "studio_available", "Studio Available", FieldType.BOOLEAN, false, true, 5, null, "Do you have access to a studio?")
        );
        artistTypeFieldRepository.saveAll(fields);
    }

    private void createVideographerFields(ArtistType videographer) {
        List<ArtistTypeField> fields = Arrays.asList(
            createField(videographer, "video_types", "Video Types", FieldType.MULTI_SELECT, true, true, 1, null, "Types of videos you create"),
            createField(videographer, "equipment_owned", "Equipment Owned", FieldType.MULTI_SELECT, false, true, 2, null, "Video equipment you own"),
            createField(videographer, "editing_software", "Editing Software", FieldType.MULTI_SELECT, false, true, 3, null, "Video editing software you use"),
            createField(videographer, "portfolio", "Portfolio", FieldType.URL, true, false, 4, "https://", "Link to your video portfolio"),
            createField(videographer, "drone_license", "Drone License", FieldType.BOOLEAN, false, true, 5, null, "Do you have a drone pilot license?")
        );
        artistTypeFieldRepository.saveAll(fields);
    }

    private ArtistTypeField createField(ArtistType artistType, String fieldName, String displayName, 
                                       FieldType fieldType, boolean isRequired, boolean isSearchable, 
                                       int sortOrder, String placeholder, String helpText) {
        ArtistTypeField field = new ArtistTypeField();
        field.setArtistType(artistType);
        field.setFieldName(fieldName);
        field.setDisplayName(displayName);
        field.setFieldType(fieldType);
        field.setIsRequired(isRequired);
        field.setIsSearchable(isSearchable);
        field.setSortOrder(sortOrder);
        field.setPlaceholder(placeholder);
        field.setHelpText(helpText);
        field.setIsActive(true);
        return field;
    }
}

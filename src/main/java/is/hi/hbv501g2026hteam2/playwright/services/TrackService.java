package is.hi.hbv501g2026hteam2.playwright.services;

import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;

public interface TrackService {

    Track store(UUID userId, MultipartFile file);

    boolean delete(UUID id);

    Optional<Track> get(UUID id);

    Set<Track> getFromUser(UUID userId);

    Set<Track> getFromUserWithMetadata(UUID userId, TrackMetadata metadata);

    Track updateMetadata(Track track, TrackMetadata newMetadata);

    InputStream getFileContents(Track track);

}

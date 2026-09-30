package is.hi.hbv501g2026hteam2.playwright.services;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;

public interface TrackService {
    Track store(UUID userId, MultipartFile file);

    void delete(Track track);

    Track get(UUID id);

    List<Track> getFromUser(UUID userId);

    List<Track> getFromUserWithMetadata(UUID userId, TrackMetadata metadata);

    Track updateMetadata(Track track, TrackMetadata newMetadata);

    InputStream getFileContents(Track track);
}

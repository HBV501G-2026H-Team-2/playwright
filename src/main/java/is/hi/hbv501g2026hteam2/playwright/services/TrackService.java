package is.hi.hbv501g2026hteam2.playwright.services;

import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;

public interface TrackService {

    Track store(User user, MultipartFile file);

    boolean delete(UUID id);

    Optional<Track> get(UUID id);

    Set<Track> getFromUser(User user);

    Set<Track> getFromUserWithMetadata(User user, TrackMetadata metadata);

    Track updateMetadata(Track track, TrackMetadata newMetadata);

    InputStream getFileContents(Track track);

}

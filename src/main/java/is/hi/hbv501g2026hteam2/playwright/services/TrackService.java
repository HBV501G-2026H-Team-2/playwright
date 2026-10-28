package is.hi.hbv501g2026hteam2.playwright.services;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;

public interface TrackService {

    Track store(User user, String filename, InputStream contents);

    boolean delete(UUID id);

    Optional<Track> get(UUID id);

    Page<Track> getFromUser(User user, Pageable pageable);

    Page<Track> getFromUserWithMetadata(User user, TrackMetadata metadata, Pageable pageable);

    Track updateMetadata(Track track, TrackMetadata newMetadata);

    InputStream getFileContents(Track track);

}

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

    boolean delete(User user, UUID id);

    Optional<Track> get(User user, UUID id);

    Page<Track> getAll(User user, TrackMetadata metadata, Pageable pageable);

    Optional<Track> updateMetadata(User user, UUID id, TrackMetadata newMetadata);

    InputStream getFileContents(UUID id);

}

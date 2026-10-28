package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import is.hi.hbv501g2026hteam2.playwright.persistence.repositories.TrackRepository;
import is.hi.hbv501g2026hteam2.playwright.services.AudioStorageService;
import is.hi.hbv501g2026hteam2.playwright.services.TrackService;

@Service
public class TrackServiceImplementation implements TrackService {

    private final TrackRepository repository;
    private final AudioStorageService audioStorageService;

    @Autowired
    public TrackServiceImplementation(
            TrackRepository repository,
            AudioStorageService audioStorageService) {
        this.repository = repository;
        this.audioStorageService = audioStorageService;
    }

    @Override
    public Track store(User user, String filename, InputStream contents) {
        var track = new Track(user, filename);
        track = repository.save(track);

        try {
            audioStorageService.addToStorage(track.getId(), contents);

            var metadata = audioStorageService.extractMetadata(track.getId());
            track.setMetadata(metadata);

            return repository.save(track);
        } catch (IOException e) {
            delete(track.getId());
            throw new RuntimeException("could not store audio file", e);
        }
    }

    @Override
    public boolean delete(UUID id) {
        try {
            audioStorageService.deleteFromStorage(id);
        } catch (IOException e) {
            // Ignore failed storage deletion, reap later
        }

        if (!repository.existsById(id))
            return false;

        repository.deleteById(id);
        return true;
    }

    @Override
    public Optional<Track> get(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Page<Track> getFromUser(User user, Pageable pageable) {
        return repository.findByOwner(user, pageable);
    }

    @Override
    public Page<Track> getFromUserWithMetadata(User user, TrackMetadata metadata, Pageable pageable) {
        return null;
    }

    @Override
    public Track updateMetadata(Track track, TrackMetadata newMetadata) {
        return null;
    }

    @Override
    public InputStream getFileContents(Track track) {
        try {
            return audioStorageService.getFromStorage(track.getId());
        } catch (IOException e) {
            throw new RuntimeException("could not get file contents", e);
        }
    }

}

package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;
import is.hi.hbv501g2026hteam2.playwright.persistence.repositories.TrackRepository;
import is.hi.hbv501g2026hteam2.playwright.services.AudioStorageService;
import is.hi.hbv501g2026hteam2.playwright.services.TrackService;
import is.hi.hbv501g2026hteam2.playwright.services.UserService;

@Service
public class TrackServiceImplementation implements TrackService {

    private final TrackRepository repository;
    private final UserService userService;
    private final AudioStorageService audioStorageService;

    @Autowired
    public TrackServiceImplementation(
            TrackRepository repository,
            UserService userService,
            AudioStorageService audioStorageService) {
        this.repository = repository;
        this.userService = userService;
        this.audioStorageService = audioStorageService;
    }

    @Override
    public Track store(UUID userId, MultipartFile file) {
        var user = userService.get(userId).orElseThrow();
        var track = new Track(user, file.getOriginalFilename());

        try {
            audioStorageService.addToStorage(track.getId(), file.getInputStream());

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
    public Set<Track> getFromUser(UUID userId) {
        return Streamable.of(repository.findByUserId(userId)).toSet();
    }

    @Override
    public Set<Track> getFromUserWithMetadata(UUID userId, TrackMetadata metadata) {
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

package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;
import is.hi.hbv501g2026hteam2.playwright.persistence.repositories.TrackRepository;
import is.hi.hbv501g2026hteam2.playwright.services.TrackService;
import is.hi.hbv501g2026hteam2.playwright.services.AudioStorageService;

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
    public void delete(Track track) {
        repository.delete(track);
    }

    @Override
    public Track get(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Track> getFromUser(UUID userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public Track store(UUID userId, MultipartFile file) {
        try {
            Path filepath = audioStorageService.addToStorage(
                    file.getOriginalFilename(),
                    file.getInputStream());

            Track track = new Track(
                    userId,
                    filepath.toString(),
                    null);

            return repository.save(track);

        } catch (IOException e) {
            throw new RuntimeException("Could not store audio file", e);
        }
    }

    @Override
    public List<Track> getFromUserWithMetadata(UUID userId, TrackMetadata metadata) {
        return null;
    }

    @Override
    public Track updateMetadata(Track track, TrackMetadata newMetadata) {
        return null;
    }

    @Override
    public InputStream getFileContents(Track track) {
        return null;
    }

}

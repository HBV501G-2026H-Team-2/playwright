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
            delete(user, track.getId());
            throw new RuntimeException("could not store audio file", e);
        }
    }

    @Override
    public boolean delete(User user, UUID id) {
        Optional<Track> track = get(user, id);
        if (track.isEmpty())
            return false;

        try {
            audioStorageService.deleteFromStorage(id);
        } catch (IOException e) {
            // Ignore failed storage deletion, reap later
        }

        repository.delete(track.get());
        return true;
    }

    @Override
    public Optional<Track> get(User user, UUID id) {
        return repository.findById(id).filter(playlist -> playlist.isVisibleTo(user));
    }

    @Override
    public Page<Track> getAll(User user, Pageable pageable) {
        return repository.findByOwner(user, pageable);
    }

    @Override
    public Page<Track> getFromUserWithMetadata(User user, TrackMetadata metadata, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Track> updateMetadata(User user, UUID id, TrackMetadata newMetadata) {
        Optional<Track> trackOpt = get(user, id);
        if (trackOpt.isEmpty())
            return Optional.empty();

        Track track = trackOpt.get();
        TrackMetadata metadata = track.getMetadata();

        // Note: duration is excluded since it depends on the actual file contents
        if (newMetadata.getTitle() != null)
            metadata.setTitle(newMetadata.getTitle());
        if (newMetadata.getArtist() != null)
            metadata.setArtist(newMetadata.getArtist());
        if (newMetadata.getGenre() != null)
            metadata.setGenre(newMetadata.getGenre());
        if (newMetadata.getTitle() != null)
            metadata.setAlbumName(newMetadata.getAlbumName());
        if (newMetadata.getAlbumName() != null)
            metadata.setTitle(newMetadata.getTitle());
        if (newMetadata.getComment() != null)
            metadata.setComment(newMetadata.getComment());
        if (newMetadata.getYear() != null)
            metadata.setYear(newMetadata.getYear());

        track.setMetadata(metadata);
        try {
            audioStorageService.writeMetadata(id, metadata);
        } catch (IOException e) {
            // Ignore failed file update, defer to metadata in database
        }

        return Optional.of(repository.save(track));
    }

    @Override
    public InputStream getFileContents(UUID id) {
        try {
            return audioStorageService.getFromStorage(id);
        } catch (IOException e) {
            throw new RuntimeException("could not get file contents", e);
        }
    }

}

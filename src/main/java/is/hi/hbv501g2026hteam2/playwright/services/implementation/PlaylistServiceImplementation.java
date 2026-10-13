package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import is.hi.hbv501g2026hteam2.playwright.persistence.repositories.PlaylistRepository;
import is.hi.hbv501g2026hteam2.playwright.services.PlaylistService;

@Service
public class PlaylistServiceImplementation implements PlaylistService {

    private PlaylistRepository repository;

    @Autowired
    public PlaylistServiceImplementation(PlaylistRepository repository) {
        this.repository = repository;
    }

    public Playlist create(UUID userId, Playlist playlist) {
        return repository.save(playlist);
    }

    public void delete(Playlist playlist) {
        repository.delete(playlist);
    }

    public Playlist get(UUID id) {
        return repository.findById(id).orElse(null);
    }

    public Set<Playlist> getFromUser(User user) {
        return Streamable.of(repository.findByOwner(user)).toSet();
    }

    public Set<Playlist> getContainingTrack(UUID trackId) {
        return Streamable.of(repository.findByTracks_Id(trackId)).toSet();
    }

    public Playlist updateName(Playlist playlist, String newName) {
        return null;
    }

    public Playlist updateTracks(Playlist playlist, List<Track> newTracks) {
        return null;
    }

}

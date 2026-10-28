package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import is.hi.hbv501g2026hteam2.playwright.dto.PlaylistRequest;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import is.hi.hbv501g2026hteam2.playwright.persistence.repositories.PlaylistRepository;
import is.hi.hbv501g2026hteam2.playwright.services.PlaylistService;
import is.hi.hbv501g2026hteam2.playwright.services.TrackService;

@Service
public class PlaylistServiceImplementation implements PlaylistService {

    private PlaylistRepository repository;
    private TrackService trackService;

    @Autowired
    public PlaylistServiceImplementation(PlaylistRepository repository, TrackService trackService) {
        this.repository = repository;
        this.trackService = trackService;
    }

    public Playlist create(User user, PlaylistRequest request) {
        List<Track> tracks = request.getTrackIds()
                .stream()
                .map(id -> trackService.get(id).orElse(null))
                .filter(track -> track != null && user.getId().equals(track.getOwner().getId()))
                .toList();

        Playlist playlist = new Playlist(user, request.getName(), tracks);

        return repository.save(playlist);
    }

    public boolean delete(UUID id) {
        if (!repository.existsById(id))
            return false;

        repository.deleteById(id);
        return true;
    }

    public Optional<Playlist> get(UUID id) {
        return repository.findById(id);
    }

    public Set<Playlist> getFromUser(User user) {
        return Streamable.of(repository.findByOwner(user)).toSet();
    }

    public Set<Playlist> getContainingTrack(UUID trackId) {
        return Streamable.of(repository.findByTracks_Id(trackId)).toSet();
    }

    public Playlist update(Playlist playlist, PlaylistRequest request) {
        playlist.setName(request.getName());

        List<Track> tracks = request.getTrackIds()
                .stream()
                .map(id -> trackService.get(id).orElse(null))
                .filter(track -> track != null && playlist.getOwner().getId().equals(track.getOwner().getId()))
                .toList();

        playlist.setTracks(tracks);

        return repository.save(playlist);
    }

}

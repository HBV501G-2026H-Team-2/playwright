package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import is.hi.hbv501g2026hteam2.playwright.dto.AutoplaylistRequest;
import is.hi.hbv501g2026hteam2.playwright.dto.PlaylistRequest;
import is.hi.hbv501g2026hteam2.playwright.observer.AutoplaylistUpdater;
import is.hi.hbv501g2026hteam2.playwright.observer.TrackMetadataObservable;
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
    public PlaylistServiceImplementation(PlaylistRepository repository, TrackService trackService, TrackMetadataObservable trackMetadataObservable) {
        this.repository = repository;
        this.trackService = trackService;

        trackMetadataObservable.addObserver(new AutoplaylistUpdater(this));
    }

    @Override
    public Playlist create(User user, PlaylistRequest request) {
        List<Track> tracks = request.getTrackIds()
                .stream()
                .flatMap(id -> trackService.get(user, id).stream())
                .toList();

        Playlist playlist = new Playlist(user, request.getName(), tracks);

        return repository.save(playlist);
    }

    @Override
    public Playlist createAuto(User user, AutoplaylistRequest request) {
        Page<Track> tracks = trackService.getAll(user, request.getMetadata(), Pageable.unpaged());

        Playlist playlist = new Playlist(user, request.getName(), tracks.getContent(), request.getMetadata());

        return repository.save(playlist);
    }

    @Override
    public boolean delete(User user, UUID id) {
        Optional<Playlist> playlist = get(user, id);
        if (playlist.isEmpty())
            return false;

        repository.delete(playlist.get());
        return true;
    }

    @Override
    public Optional<Playlist> get(User user, UUID id) {
        return repository.findById(id).filter(playlist -> playlist.isVisibleTo(user));
    }

    @Override
    public Page<Playlist> getAll(User user, String name, Boolean auto, Pageable pageable) {
        if (user == null)
            return Page.empty();

        Playlist examplePlaylist = new Playlist(user, name, null);
        examplePlaylist.setIsAuto(auto);

        Example<Playlist> probe = Example.of(examplePlaylist);
        return repository.findAll(probe, pageable);
    }

    @Override
    public Page<Playlist> getContainingTrack(User user, UUID trackId, Pageable pageable) {
        if (trackService.get(user, trackId).isEmpty())
            return Page.empty();

        return repository.findByTracks_Id(trackId, pageable);
    }

    @Override
    public Optional<Playlist> update(User user, UUID id, PlaylistRequest request) {
        Optional<Playlist> playlistOpt = get(user, id);
        if (playlistOpt.isEmpty())
            return Optional.empty();

        Playlist playlist = playlistOpt.get();

        if (request.getName() != null)
            playlist.setName(request.getName());

        if (request.getTrackIds() != null) {
            List<Track> tracks = request.getTrackIds()
                    .stream()
                    .flatMap(trackId -> trackService.get(user, trackId).stream())
                    .toList();

            playlist.setTracks(tracks);
        }

        return Optional.of(repository.save(playlist));
    }

    @Override
    public Playlist addTrack(Playlist playlist, Track track) {
        if (!track.isVisibleTo(playlist.getOwner()))
            return playlist;

        List<Track> tracks = playlist.getTracks();
        tracks.add(track);

        playlist.setTracks(tracks);
        return repository.save(playlist);
    }

    @Override
    public Playlist removeTrack(Playlist playlist, Track track) {
        if (!track.isVisibleTo(playlist.getOwner()))
            return playlist;

        List<Track> tracks = playlist.getTracks();
        tracks.removeIf(t -> t.getId().equals(track.getId()));

        playlist.setTracks(tracks);
        return repository.save(playlist);
    }

}

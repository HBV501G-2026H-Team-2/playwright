package is.hi.hbv501g2026hteam2.playwright.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;

public interface PlaylistService {

    Playlist create(UUID userId, Playlist playlist);

    void delete(Playlist playlist);

    Playlist get(UUID id);

    Set<Playlist> getFromUser(UUID userId);

    Set<Playlist> getContainingTrack(UUID trackId);

    Playlist updateName(Playlist playlist, String newName);

    Playlist updateTracks(Playlist playlist, List<Track> newTracks);

}

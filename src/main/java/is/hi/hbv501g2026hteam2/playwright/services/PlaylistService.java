package is.hi.hbv501g2026hteam2.playwright.services;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import is.hi.hbv501g2026hteam2.playwright.dto.PlaylistRequest;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;

public interface PlaylistService {

    Playlist create(User user, PlaylistRequest request);

    boolean delete(UUID id);

    Optional<Playlist> get(UUID id);

    Set<Playlist> getFromUser(User user);

    Set<Playlist> getContainingTrack(UUID trackId);

    Playlist update(Playlist playlist, PlaylistRequest request);

}

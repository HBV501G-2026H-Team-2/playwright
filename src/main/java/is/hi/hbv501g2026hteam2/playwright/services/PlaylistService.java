package is.hi.hbv501g2026hteam2.playwright.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import is.hi.hbv501g2026hteam2.playwright.dto.PlaylistRequest;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;

public interface PlaylistService {

    Playlist create(User user, PlaylistRequest request);

    boolean delete(UUID id);

    Optional<Playlist> get(UUID id);

    Page<Playlist> getFromUser(User user, Pageable pageable);

    Page<Playlist> getContainingTrack(UUID trackId, Pageable pageable);

    Playlist update(Playlist playlist, PlaylistRequest request);

}

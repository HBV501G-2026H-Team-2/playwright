package is.hi.hbv501g2026hteam2.playwright.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import is.hi.hbv501g2026hteam2.playwright.dto.AutoplaylistRequest;
import is.hi.hbv501g2026hteam2.playwright.dto.PlaylistRequest;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;

public interface PlaylistService {

    Playlist create(User user, PlaylistRequest request);

    Playlist createAuto(User user, AutoplaylistRequest request);

    boolean delete(User user, UUID id);

    Optional<Playlist> get(User user, UUID id);

    Page<Playlist> getAll(User user, String name, Boolean auto, Pageable pageable);

    Page<Playlist> getContainingTrack(User user, UUID trackId, Pageable pageable);

    Optional<Playlist> update(User user, UUID id, PlaylistRequest request);

    Playlist addTrack(Playlist playlist, Track track);

    Playlist removeTrack(Playlist playlist, Track track);

}

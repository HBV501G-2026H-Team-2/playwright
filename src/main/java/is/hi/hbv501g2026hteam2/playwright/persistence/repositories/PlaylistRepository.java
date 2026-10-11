package is.hi.hbv501g2026hteam2.playwright.persistence.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {

    Iterable<Playlist> findByUserId(UUID userId);

    Iterable<Playlist> findByTrackId(UUID trackId);

}

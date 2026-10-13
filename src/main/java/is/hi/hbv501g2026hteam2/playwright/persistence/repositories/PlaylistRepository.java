package is.hi.hbv501g2026hteam2.playwright.persistence.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {

    Iterable<Playlist> findByOwner(User owner);

    Iterable<Playlist> findByTracks_Id(UUID trackId);

}

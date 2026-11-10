package is.hi.hbv501g2026hteam2.playwright.persistence.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {

    Page<Playlist> findByTracks_Id(UUID trackId, Pageable pageable);

}

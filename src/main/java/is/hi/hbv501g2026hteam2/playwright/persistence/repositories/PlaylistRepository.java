package is.hi.hbv501g2026hteam2.playwright.persistence.repositories;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist; 
import org.springframework.data.jpa.repository.JpaRepository; 
import java.util.Optional; 
import java.util.UUID; 
import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {
    Playlist save(Playlist playlist); 

    void delete(Playlist playlist); 

    Optional<Playlist> findById(UUID id); 

    List<Playlist> findAllByUserId(UUID userId); 

    List<Playlist> findAllByTracksId(UUID trackId); 
}
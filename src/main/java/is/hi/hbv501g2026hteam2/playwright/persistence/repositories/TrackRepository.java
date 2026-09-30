package is.hi.hbv501g2026hteam2.playwright.persistence.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;

public interface TrackRepository extends CrudRepository<Track, UUID> {

    List<Track> findAllByUserId(UUID userId);
}

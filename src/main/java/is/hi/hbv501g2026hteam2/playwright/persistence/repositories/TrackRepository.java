package is.hi.hbv501g2026hteam2.playwright.persistence.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;

public interface TrackRepository extends JpaRepository<Track, UUID> {

    Iterable<Track> findByOwner(User owner);

}

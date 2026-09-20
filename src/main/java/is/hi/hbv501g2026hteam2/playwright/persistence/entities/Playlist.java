package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import java.util.UUID;
import java.util.List;
import jakarta.persistence.Entity; 

@Entity
public final class Playlist{
    private UUID id; 
    private UUID userId; 
    private String name; 
    private List<Track> tracks;
}
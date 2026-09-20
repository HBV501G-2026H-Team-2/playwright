package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;
import jakarta.persistence.Entity; 
import java.util.UUID;
import java.nio.file.Path;

@Entity
public final class Track {
    private UUID id; 
    private UUID userId; 
    private Path filepath; 
    private TrackMetadata metadata; 

    public Track(UUID id, UUID userId, Path filepath, TrackMetadata metadata){
        this.id = UUID.randomUUID();
        this.userId = userId; 
        this.filepath = filepath; 
        this.metadata = metadata; 
    }

    public UUID getId(){
        return id; 
    }

    public UUID getUserId(){
        return userId; 
    }

    public Path getFilePath(){
        return filepath; 
    }

    public TrackMetadata getMetadata(){
        return metadata; 
    }

}
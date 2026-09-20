package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;
import jakarta.persistence.Entity; 
import jakarta.persistence.Id; 
import jakarta.persistence.Embedded; 
import jakarta.persistence.Transient; 
import java.util.UUID;
import java.nio.file.Path;

@Entity
public final class Track {
    @Id
    private UUID id; 
    private UUID userId; 
    @Embedded
    private TrackMetadata metadata; 

    public Track(UUID userId, TrackMetadata metadata){
        this.id = UUID.randomUUID();
        this.userId = userId; 
        this.metadata = metadata; 
    }

    public UUID getId(){
        return id; 
    }

    public UUID getUserId(){
        return userId; 
    }

    public TrackMetadata getMetadata(){
        return metadata; 
    }

}
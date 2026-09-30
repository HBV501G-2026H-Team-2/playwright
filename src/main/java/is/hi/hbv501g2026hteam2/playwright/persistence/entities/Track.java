package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Embedded;
import java.util.UUID;

@Entity
public final class Track {
    @Id
    private UUID id;
    private UUID userId;
    private String filepath;
    @Embedded
    private TrackMetadata metadata;

    protected Track() {
    }

    public Track(UUID userId, String filepath, TrackMetadata metadata) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.filepath = filepath;
        this.metadata = metadata;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getFilepath() {
        return filepath;
    }

    public TrackMetadata getMetadata() {
        return metadata;
    }
}

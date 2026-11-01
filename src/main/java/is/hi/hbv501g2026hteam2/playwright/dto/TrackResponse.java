package is.hi.hbv501g2026hteam2.playwright.dto;

import java.util.UUID;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;

public class TrackResponse {

    private UUID id;
    private String filename;
    private TrackMetadata metadata;

    public TrackResponse(UUID id, String filename, TrackMetadata metadata) {
        this.id = id;
        this.filename = filename;
        this.metadata = metadata;
    }

    public TrackResponse(Track track) {
        this.id = track.getId();
        this.filename = track.getFilename();
        this.metadata = track.getMetadata();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public TrackMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(TrackMetadata metadata) {
        this.metadata = metadata;
    }

}

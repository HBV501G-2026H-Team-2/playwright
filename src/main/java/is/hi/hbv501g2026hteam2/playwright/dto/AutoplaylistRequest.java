package is.hi.hbv501g2026hteam2.playwright.dto;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;

public class AutoplaylistRequest {

    private String name;
    private TrackMetadata metadata;

    public AutoplaylistRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrackMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(TrackMetadata metadata) {
        this.metadata = metadata;
    }

}

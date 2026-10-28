package is.hi.hbv501g2026hteam2.playwright.dto;

import java.util.List;
import java.util.UUID;

public class CreatePlaylistRequest {

    private String name;
    private List<UUID> trackIds;

    public CreatePlaylistRequest(String name, List<UUID> trackIds) {
        this.name = name;
        this.trackIds = trackIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UUID> getTrackIds() {
        return trackIds;
    }

    public void setTrackIds(List<UUID> trackIds) {
        this.trackIds = trackIds;
    }

}

package is.hi.hbv501g2026hteam2.playwright.dto;

import java.util.List;
import java.util.UUID;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;

public class PlaylistResponse {

    private UUID id;
    private String name;
    private List<UUID> trackIds;

    public PlaylistResponse(UUID id, String name, List<UUID> trackIds) {
        this.id = id;
        this.name = name;
        this.trackIds = trackIds;
    }

    public PlaylistResponse(Playlist playlist) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.trackIds = playlist.getTracks().stream().map(track -> track.getId()).toList();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

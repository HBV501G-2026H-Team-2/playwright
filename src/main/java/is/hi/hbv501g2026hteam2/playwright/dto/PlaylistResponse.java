package is.hi.hbv501g2026hteam2.playwright.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;

public class PlaylistResponse {

    private UUID id;
    private String name;
    private List<UUID> trackIds;
    private boolean isAuto;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TrackMetadata autoCriteria;

	public PlaylistResponse(UUID id, String name, List<UUID> trackIds, TrackMetadata autoCriteria) {
        this.id = id;
        this.name = name;
        this.trackIds = trackIds;
        this.isAuto = autoCriteria != null;
        this.autoCriteria = autoCriteria;
    }

    public PlaylistResponse(Playlist playlist) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.trackIds = playlist.getTracks().stream().map(track -> track.getId()).toList();
        this.isAuto = playlist.getAutoCriteria() != null;
        this.autoCriteria = this.isAuto ? playlist.getAutoCriteria().getMetadata() : null;
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

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    public TrackMetadata getAutoCriteria() {
		return autoCriteria;
	}

	public void setAutoCriteria(TrackMetadata autoCriteria) {
		this.autoCriteria = autoCriteria;
	}

}

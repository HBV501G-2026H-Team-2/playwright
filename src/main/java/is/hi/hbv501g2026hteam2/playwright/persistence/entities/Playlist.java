package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;

@Entity
public final class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User owner;

    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "playlist_tracks", joinColumns = @JoinColumn(name = "playlist_id"), inverseJoinColumns = @JoinColumn(name = "track_id"))
    @OrderColumn(nullable = false)
    private List<Track> tracks;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isAuto;

	@OneToOne(mappedBy = "playlist", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private PlaylistAutoCriteria autoCriteria;

    protected Playlist() {
    }

    public Playlist(User owner, String name, List<Track> tracks) {
        this.owner = owner;
        this.name = name;
        this.tracks = tracks;
        this.isAuto = false;
        this.autoCriteria = null;
    }

    public Playlist(User owner, String name, List<Track> tracks, TrackMetadata autoCriteria) {
        this.owner = owner;
        this.name = name;
        this.tracks = tracks;
        this.isAuto = true;
        this.autoCriteria = new PlaylistAutoCriteria(this, autoCriteria);
    }

    public boolean isVisibleTo(User user) {
        return user.isAdmin() || user.getId().equals(owner.getId());
    }

    public UUID getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public Boolean getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(Boolean isAuto) {
        this.isAuto = isAuto;
    }

    public PlaylistAutoCriteria getAutoCriteria() {
        return autoCriteria;
    }

    public void setAutoCriteria(PlaylistAutoCriteria autoCriteria) {
        this.autoCriteria = autoCriteria;
    }

}

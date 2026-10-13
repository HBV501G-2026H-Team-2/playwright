package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public final class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User owner;

    private String filename;

    @Embedded
    private TrackMetadata metadata;

    protected Track() {
    }

    public Track(User owner, String filename) {
        this.owner = owner;
        this.filename = filename;
    }

    public Track(User owner, String filename, TrackMetadata metadata) {
        this.owner = owner;
        this.filename = filename;
        this.metadata = metadata;
    }

    public UUID getId() {
        return id;
    }

    public User getOwner() {
        return owner;
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

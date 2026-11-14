package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import java.util.UUID;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
public class PlaylistAutoCriteria {

    @Id
    private UUID playlistId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @Embedded
    private TrackMetadata metadata;

    protected PlaylistAutoCriteria() {
    }

    public PlaylistAutoCriteria(Playlist playlist, TrackMetadata metadata) {
        this.playlist = playlist;
        this.metadata = metadata;
    }

    public boolean matches(TrackMetadata metadata) {
        return (this.metadata.getTitle() == null || this.metadata.getTitle().equals(metadata.getTitle()))
                && (this.metadata.getArtist() == null || this.metadata.getArtist().equals(metadata.getArtist()))
                && (this.metadata.getGenre() == null || this.metadata.getGenre().equals(metadata.getGenre()))
                && (this.metadata.getAlbumName() == null || this.metadata.getAlbumName().equals(metadata.getAlbumName()))
                && (this.metadata.getComment() == null || this.metadata.getComment().equals(metadata.getComment()))
                && (this.metadata.getDuration() == null || this.metadata.getDuration().equals(metadata.getDuration()))
                && (this.metadata.getYear() == null || this.metadata.getYear().equals(metadata.getYear()));
    }

    public UUID getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(UUID playlistId) {
        this.playlistId = playlistId;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public TrackMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(TrackMetadata metadata) {
        this.metadata = metadata;
    }

}

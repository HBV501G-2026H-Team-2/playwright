package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import java.time.Duration;
import java.util.Date;

import jakarta.persistence.Embeddable;

@Embeddable
public final class TrackMetadata {

    private String title;
    private String artist;
    private String genre;
    private String albumName;
    private String comment;
    private Duration duration;
    private Date releaseDate;

    public TrackMetadata(String title, String artist, String genre, String albumName, Date releaseDate,
            Duration duration, String comment) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.albumName = albumName;
        this.comment = comment;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

}

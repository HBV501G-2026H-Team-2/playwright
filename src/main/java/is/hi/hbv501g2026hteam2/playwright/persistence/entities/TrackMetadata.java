package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import jakarta.persistence.Entity; 
import java.util.UUID;
import java.nio.file.Path;
import java.awt.Image; 
import java.time.Duration; 
import java.util.Date; 

public final class TrackMetadata {
    private String title; 
    private String artist; 
    private String genre; 
    private String albumName; 
    private String comment; 
    private Image albumArt; 
    private Duration duration; 
    private Date releaseDate; 

    public TrackMetadata(String title, String artist, String genre, String albumName, Image albumArt, Date releaseDate, Duration duration, String comment){
        this.title = title; 
        this.artist = artist; 
        this.genre = genre; 
        this.albumName = albumName; 
        this.comment = comment; 
        this.albumArt = albumArt; 
        this.duration = duration; 
        this.releaseDate = releaseDate; 
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public String getAlbumName() {
        return albumName;
    }

    public Image getAlbumArt() {
        return albumArt;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getComment(){
        return comment;
    }
}
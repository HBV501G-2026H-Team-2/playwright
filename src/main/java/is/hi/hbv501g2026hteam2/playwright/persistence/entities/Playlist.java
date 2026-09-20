package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;

import java.util.UUID;
import java.util.List;
import jakarta.persistence.Entity; 
import jakarta.persistence.ManyToMany; 
import jakarta.persistence.Id; 

@Entity
public final class Playlist{
    @Id
    private UUID id; 
    private UUID userId; 
    private String name; 
    @ManyToMany
    private List<Track> tracks;

    public  Playlist(UUID userId, String name, List<Track> tracks){
        this.id = UUID.randomUUID();
        this.userId = userId; 
        this.name = name; 
        this.tracks = tracks; 
    }

    public UUID getId(){
        return id; 
    }

    public UUID getUserId(){
        return userId; 
    }

    public String getName(){
        return name;
    }

    public List<Track> getTracks(){
        return tracks; 
    }

}
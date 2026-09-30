package is.hi.hbv501g2026hteam2.playwright.services;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist; 
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User; 
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track; 
import java.util.List;
import java.util.UUID; 

public interface PlaylistService {
    Playlist create(UUID userId, Playlist playlist); 

    void delete(Playlist playlist); 

    Playlist get(UUID id); 

    List<Playlist> getFromUser(UUID userId); 

    List<Playlist> getContainingTrack(UUID trackId); 

    Playlist updateName(Playlist playlist, String newName); 

    Playlist updateTracks(Playlist playlist, List<Track> newTracks); 
}
package is.hi.hbv501g2026hteam2.playwright.observer;

import java.util.List;

import org.springframework.data.domain.Pageable;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Playlist;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;
import is.hi.hbv501g2026hteam2.playwright.services.PlaylistService;

public class AutoplaylistUpdater implements TrackMetadataObserver {

    private final PlaylistService playlistService;

    public AutoplaylistUpdater(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    public void handleNew(Track track) {
        List<Playlist> autoplaylists = playlistService
                .getAll(track.getOwner(), null, true, Pageable.unpaged())
                .getContent();

        for (Playlist playlist : autoplaylists)
            if (playlist.getAutoCriteria().matches(track.getMetadata()))
                playlistService.addTrack(playlist, track);
    }

    public void handleChanged(Track track, TrackMetadata oldMetadata) {
        List<Playlist> autoplaylists = playlistService
                .getAll(track.getOwner(), null, true, Pageable.unpaged())
                .getContent();

        for (Playlist playlist : autoplaylists) {
            boolean matchesOld = playlist.getAutoCriteria().matches(oldMetadata);
            boolean matchesNew = playlist.getAutoCriteria().matches(track.getMetadata());
            if (!matchesOld && matchesNew)
                playlistService.addTrack(playlist, track);
            else if (matchesOld && !matchesNew)
                playlistService.removeTrack(playlist, track);
        }
    }

}

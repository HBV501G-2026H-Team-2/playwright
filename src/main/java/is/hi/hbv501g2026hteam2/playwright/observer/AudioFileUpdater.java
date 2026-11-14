package is.hi.hbv501g2026hteam2.playwright.observer;

import java.io.IOException;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;
import is.hi.hbv501g2026hteam2.playwright.services.AudioStorageService;

public class AudioFileUpdater implements TrackMetadataObserver {

    private final AudioStorageService audioStorageService;

    public AudioFileUpdater(AudioStorageService audioStorageService) {
        this.audioStorageService = audioStorageService;
    }

    public void handleNew(Track track) {
        // Metadata extracted from file on upload, no action needed
    }

    public void handleChanged(Track track, TrackMetadata _oldMetadata) {
        try {
            audioStorageService.writeMetadata(track.getId(), track.getMetadata());
        } catch (IOException e) {
            throw new RuntimeException("could not update file with metadata", e);
        }
    }

}

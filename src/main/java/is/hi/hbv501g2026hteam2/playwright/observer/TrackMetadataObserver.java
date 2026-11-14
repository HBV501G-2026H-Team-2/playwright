package is.hi.hbv501g2026hteam2.playwright.observer;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;

public interface TrackMetadataObserver {

    void handleNew(Track track);

    void handleChanged(Track track, TrackMetadata oldMetadata);

}

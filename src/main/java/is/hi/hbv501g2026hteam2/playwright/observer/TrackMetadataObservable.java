package is.hi.hbv501g2026hteam2.playwright.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;

@Component
public class TrackMetadataObservable {

    private final List<TrackMetadataObserver> observers = new CopyOnWriteArrayList<TrackMetadataObserver>();

    public void addObserver(TrackMetadataObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TrackMetadataObserver observer) {
        observers.remove(observer);
    }

    public void reportNew(Track track) {
        for (var observer : observers)
            observer.handleNew(track);
    }

    public void reportChanged(Track track, TrackMetadata oldMetadata) {
        for (var observer : observers)
            observer.handleChanged(track, oldMetadata);
    }

}

package is.hi.hbv501g2026hteam2.playwright.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;

public interface AudioStorageService {
    Path addToStorage(String filename, InputStream contents) throws IOException;

    TrackMetadata extractMetadata(Path filepath);

    InputStream getFromStorage(String filepath) throws IOException; 
}

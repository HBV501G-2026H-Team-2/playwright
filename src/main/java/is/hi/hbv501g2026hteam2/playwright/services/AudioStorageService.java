package is.hi.hbv501g2026hteam2.playwright.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;

public interface AudioStorageService {

    Path addToStorage(UUID id, InputStream contents) throws IOException;

    TrackMetadata extractMetadata(UUID id) throws IOException;

    InputStream getFromStorage(UUID id) throws IOException;

}

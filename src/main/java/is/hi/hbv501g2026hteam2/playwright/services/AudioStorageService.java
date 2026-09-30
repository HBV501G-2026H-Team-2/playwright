package is.hi.hbv501g2026hteam2.playwright.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface AudioStorageService {
    Path addToStorage(String filename, InputStream contents) throws IOException;
}

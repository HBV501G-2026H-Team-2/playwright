package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;

import is.hi.hbv501g2026hteam2.playwright.services.AudioStorageService;

@Service
public class AudioStorageServiceImplementation implements AudioStorageService {

    private final Path storageRoot = Path.of("storage");

    public AudioStorageServiceImplementation() throws IOException {
        Files.createDirectories(storageRoot);
    }

    @Override
    public Path addToStorage(String filename, InputStream contents) throws IOException {
        String storedFilename = UUID.randomUUID() + "-" + filename;
        Path destination = storageRoot.resolve(storedFilename);

        Files.copy(contents, destination, StandardCopyOption.REPLACE_EXISTING);

        return destination;
    }
}


package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AudioStorageServiceImplementationTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void storesRetrievesAndDeletesFile() throws IOException {
        // Arrange
        AudioStorageServiceImplementation storage = new AudioStorageServiceImplementation(temporaryDirectory);

        UUID id = UUID.randomUUID();

        byte[] originalContents = "Test audio contents".getBytes(StandardCharsets.UTF_8);

        // Act: store
        storage.addToStorage(
                id,
                new ByteArrayInputStream(originalContents));

        // Assert: file exists
        Path storedFile = temporaryDirectory.resolve(id.toString());
        assertTrue(Files.exists(storedFile));

        // Act + Assert: retrieve
        try (InputStream retrieved = storage.getFromStorage(id)) {
            assertArrayEquals(originalContents, retrieved.readAllBytes());
        }

        // Act + Assert: delete
        assertTrue(storage.deleteFromStorage(id));
        assertFalse(Files.exists(storedFile));
    }
}

package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.UUID;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.stereotype.Service;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;
import is.hi.hbv501g2026hteam2.playwright.services.AudioStorageService;

@Service
public class AudioStorageServiceImplementation implements AudioStorageService {

    private final Path storageRoot = Path.of("storage");

    public AudioStorageServiceImplementation() throws IOException {
        Files.createDirectories(storageRoot);
    }

    private Path resolveStoragePath(UUID id) {
        return storageRoot.resolve(id.toString());
    }

    @Override
    public Path addToStorage(UUID id, InputStream contents) throws IOException {
        Path destination = resolveStoragePath(id);

        Files.copy(contents, destination, StandardCopyOption.REPLACE_EXISTING);

        return destination;
    }

    @Override
    public TrackMetadata extractMetadata(UUID id) throws IOException {
        try {
            Path storagePath = resolveStoragePath(id);
            AudioFile audioFile = AudioFileIO.read(storagePath.toFile());

            Tag tag = audioFile.getTag();
            AudioHeader header = audioFile.getAudioHeader();

            String title = tag != null ? tag.getFirst(FieldKey.TITLE) : "";
            String artist = tag != null ? tag.getFirst(FieldKey.ARTIST) : "";
            String genre = tag != null ? tag.getFirst(FieldKey.GENRE) : "";
            String album = tag != null ? tag.getFirst(FieldKey.ALBUM) : "";
            String comment = tag != null ? tag.getFirst(FieldKey.COMMENT) : "";

            Duration duration = Duration.ofSeconds(header.getTrackLength());

            return new TrackMetadata(
                    title,
                    artist,
                    genre,
                    album,
                    null,
                    duration,
                    comment);

        } catch (Exception e) {
            throw new RuntimeException("Could not extract metadata from audio file", e);
        }
    }

    @Override
    public InputStream getFromStorage(UUID id) throws IOException {
        return Files.newInputStream(Path.of(id.toString()));
    }

}

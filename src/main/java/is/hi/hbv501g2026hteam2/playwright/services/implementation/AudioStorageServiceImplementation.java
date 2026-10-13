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
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
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
    public boolean deleteFromStorage(UUID id) throws IOException {
        Path storagePath = resolveStoragePath(id);
        return Files.deleteIfExists(storagePath);
    }

    @Override
    public TrackMetadata extractMetadata(UUID id) throws IOException {
        try {
            Path storagePath = resolveStoragePath(id);

            AudioFile audioFile = AudioFileIO.readMagic(storagePath.toFile());

            Tag tag = audioFile.getTag();
            AudioHeader header = audioFile.getAudioHeader();

            String title = tag != null ? tag.getFirst(FieldKey.TITLE) : "";
            String artist = tag != null ? tag.getFirst(FieldKey.ARTIST) : "";
            String genre = tag != null ? tag.getFirst(FieldKey.GENRE) : "";
            String album = tag != null ? tag.getFirst(FieldKey.ALBUM) : "";
            String year = tag != null ? tag.getFirst(FieldKey.YEAR) : "";
            String comment = tag != null ? tag.getFirst(FieldKey.COMMENT) : "";

            Duration duration = Duration.ofSeconds(header.getTrackLength());

            return new TrackMetadata(
                    title,
                    artist,
                    genre,
                    album,
                    year,
                    duration,
                    comment);
        } catch (CannotReadException e) {
            throw new IOException("Unable to read track file in storage: " + id.toString(), e);
        } catch (ReadOnlyFileException e) {
            throw new IOException("Encountered read-only track file in storage: " + id.toString(), e);
        } catch (TagException e) {
            throw new IOException("Encountered corrupted track file in storage: " + id.toString());
        } catch (InvalidAudioFrameException e) {
            throw new IOException("Encountered corrupted track file in storage: " + id.toString());
        }
    }

    @Override
    public InputStream getFromStorage(UUID id) throws IOException {
        Path storagePath = resolveStoragePath(id);
        return Files.newInputStream(storagePath);
    }

}

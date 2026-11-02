
package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.UUID;

import jakarta.annotation.PreDestroy;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.checksums.RequestChecksumCalculation;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.TrackMetadata;
import is.hi.hbv501g2026hteam2.playwright.services.AudioStorageService;

@Service
@ConditionalOnProperty(prefix = "playwright.storage", name = "backend", havingValue = "s3")
public class S3AudioStorageServiceImplementation implements AudioStorageService {

    private final S3Client s3;
    private final String bucket;

    public S3AudioStorageServiceImplementation(
            @Value("${playwright.storage.s3.endpoint}") String endpoint,
            @Value("${playwright.storage.s3.region:garage}") String region,
            @Value("${playwright.storage.s3.bucket:playwright}") String bucket) {

        this.bucket = bucket;

        this.s3 = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .forcePathStyle(true)
                .requestChecksumCalculation(RequestChecksumCalculation.WHEN_REQUIRED)
                .build();
    }

    private String objectKey(UUID id) {
        return id.toString();
    }

    @Override
    public void addToStorage(UUID id, InputStream contents) throws IOException {
        Path temp = Files.createTempFile("playwright-upload-", ".audio");

        try {
            Files.copy(contents, temp, StandardCopyOption.REPLACE_EXISTING);

            s3.putObject(
                    request -> request
                            .bucket(bucket)
                            .key(objectKey(id)),
                    RequestBody.fromFile(temp));
        } catch (SdkException e) {
            throw new IOException("S3 upload failed: " + id, e);
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    @Override
    public boolean deleteFromStorage(UUID id) throws IOException {
        try {
            s3.headObject(request -> request
                    .bucket(bucket)
                    .key(objectKey(id)));

            s3.deleteObject(request -> request
                    .bucket(bucket)
                    .key(objectKey(id)));

            return true;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false;
            }
            throw new IOException("S3 deletion failed: " + id, e);
        } catch (SdkException e) {
            throw new IOException("S3 deletion failed: " + id, e);
        }
    }

    @Override
    public InputStream getFromStorage(UUID id) throws IOException {
        try {
            return s3.getObject(request -> request
                    .bucket(bucket)
                    .key(objectKey(id)));
        } catch (SdkException e) {
            throw new IOException("S3 download failed: " + id, e);
        }
    }

    @Override
    public TrackMetadata extractMetadata(UUID id) throws IOException {
        Path temp = Files.createTempFile("playwright-metadata-", ".audio");

        try {
            try (InputStream contents = getFromStorage(id)) {
                Files.copy(contents, temp, StandardCopyOption.REPLACE_EXISTING);
            }

            var audioFile = AudioFileIO.readMagic(temp.toFile());
            var tag = audioFile.getTag();
            var header = audioFile.getAudioHeader();

            String title = tag != null ? tag.getFirst(FieldKey.TITLE) : "";
            String artist = tag != null ? tag.getFirst(FieldKey.ARTIST) : "";
            String genre = tag != null ? tag.getFirst(FieldKey.GENRE) : "";
            String album = tag != null ? tag.getFirst(FieldKey.ALBUM) : "";
            String year = tag != null ? tag.getFirst(FieldKey.YEAR) : "";
            String comment = tag != null ? tag.getFirst(FieldKey.COMMENT) : "";

            Duration duration = Duration.ofSeconds(header.getTrackLength());

            return new TrackMetadata(
                    title, artist, genre, album, year, duration, comment);

        } catch (CannotReadException | ReadOnlyFileException
                | TagException | InvalidAudioFrameException e) {
            throw new IOException("Audio metadata extraction failed: " + id, e);
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    @PreDestroy
    public void close() {
        s3.close();
    }
}

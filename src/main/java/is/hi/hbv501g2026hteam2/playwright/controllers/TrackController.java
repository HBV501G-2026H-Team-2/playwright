package is.hi.hbv501g2026hteam2.playwright.controllers;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import is.hi.hbv501g2026hteam2.playwright.persistence.enums.UserRole;
import is.hi.hbv501g2026hteam2.playwright.services.TrackService;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @PostMapping
    public ResponseEntity<Track> uploadTrack(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file) {
        if (user == null || user.getRole() != UserRole.User)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (file.isEmpty())
            return ResponseEntity.badRequest().body(null);

        try {
            Track track = trackService.store(user, file.getOriginalFilename(), file.getInputStream());
            URI location = URI.create("/api/tracks/" + track.getId().toString());
            return ResponseEntity.created(location).body(track);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<Track>> getTracks(@AuthenticationPrincipal User user, Pageable pageable) {
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Page<Track> tracks = trackService.getFromUser(user, pageable);
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/{trackId}")
    public ResponseEntity<Track> getTrack(@AuthenticationPrincipal User user, @PathVariable UUID trackId) {
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Track> trackOpt = trackService.get(trackId);
        if (trackOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Track track = trackOpt.get();
        if (!user.getId().equals(track.getOwner().getId()))
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(track);
    }

    @GetMapping("/{trackId}/file")
    public ResponseEntity<Resource> downloadTrack(@AuthenticationPrincipal User user, @PathVariable UUID trackId) {
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (user.getRole() != UserRole.User)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Optional<Track> trackOpt = trackService.get(trackId);
        if (trackOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Track track = trackOpt.get();
        if (!user.getId().equals(track.getOwner().getId()))
            return ResponseEntity.notFound().build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + track.getFilename() + "\"");

        var resource = new InputStreamResource(trackService.getFileContents(track));

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(resource);
    }

    @DeleteMapping(value = "/{trackId}")
    public ResponseEntity<Void> deleteTrack(@AuthenticationPrincipal User user, @PathVariable UUID trackId) {
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Track> trackOpt = trackService.get(trackId);
        if (trackOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Track track = trackOpt.get();
        if (!user.getId().equals(track.getOwner().getId()))
            return ResponseEntity.notFound().build();

        trackService.delete(track.getId());

        return ResponseEntity.noContent().build();
    }
}

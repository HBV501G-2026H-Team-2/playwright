package is.hi.hbv501g2026hteam2.playwright.controllers;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

import is.hi.hbv501g2026hteam2.playwright.dto.TrackResponse;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import is.hi.hbv501g2026hteam2.playwright.services.TrackService;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @PostMapping
    public ResponseEntity<TrackResponse> uploadTrack(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return ResponseEntity.badRequest().body(null);

        try {
            Track track = trackService.store(user, file.getOriginalFilename(), file.getInputStream());
            URI location = URI.create("/api/tracks/" + track.getId().toString());
            return ResponseEntity.created(location).body(new TrackResponse(track));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<TrackResponse>> getTracks(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 50, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Track> tracks = trackService.getAll(user, pageable);
        return ResponseEntity.ok(tracks.map(track -> new TrackResponse(track)));
    }

    @GetMapping("/{trackId}")
    public ResponseEntity<TrackResponse> getTrack(@AuthenticationPrincipal User user, @PathVariable UUID trackId) {
        Optional<Track> track = trackService.get(user, trackId);
        if (track.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(new TrackResponse(track.get()));
    }

    @GetMapping("/{trackId}/file")
    public ResponseEntity<Resource> downloadTrack(@AuthenticationPrincipal User user, @PathVariable UUID trackId) {
        Optional<Track> track = trackService.get(user, trackId);
        if (track.isEmpty())
            return ResponseEntity.notFound().build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + track.get().getFilename() + "\"");

        var resource = new InputStreamResource(trackService.getFileContents(trackId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(resource);
    }

    @DeleteMapping(value = "/{trackId}")
    public ResponseEntity<Void> deleteTrack(@AuthenticationPrincipal User user, @PathVariable UUID trackId) {
        if (!trackService.delete(user, trackId))
            return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}

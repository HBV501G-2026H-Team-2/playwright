package is.hi.hbv501g2026hteam2.playwright.controllers;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.Track;
import is.hi.hbv501g2026hteam2.playwright.services.TrackService;

@Controller
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    // TODO: Get userId from authenticated user once auth implementation is decided.
    @PostMapping("/tracks")
    @ResponseBody
    public Track uploadTrack(
            @RequestParam("userId") UUID userId,
            @RequestParam("file") MultipartFile file) {

        return trackService.store(userId, file);
    }

    @RequestMapping(value="/tracks", method=RequestMethod.GET)
    @ResponseBody 
    public Track getTrack(
            @RequestParam(value="trackId", required=true) UUID trackId, 
            Model model) {
        return trackService.get(trackId); 
    }

    // TODO: change URL, this is a placeholder
    @RequestMapping(value="/downloadTrack", method=RequestMethod.GET)
    @ResponseBody
    public Resource downloadTrack(
        @RequestParam(value="trackId", required=true) UUID trackId,
        Model model) {
            Track track = trackService.get(trackId);
            if(track == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND); 
            }
            return new InputStreamResource(trackService.getFileContents(track));
        } 
}

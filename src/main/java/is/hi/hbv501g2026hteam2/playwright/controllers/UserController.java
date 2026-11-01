package is.hi.hbv501g2026hteam2.playwright.controllers;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import is.hi.hbv501g2026hteam2.playwright.dto.UserRequest;
import is.hi.hbv501g2026hteam2.playwright.dto.UserResponse;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import is.hi.hbv501g2026hteam2.playwright.persistence.enums.UserRole;
import is.hi.hbv501g2026hteam2.playwright.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public UserController(
            UserService userService,
            AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        try {
            User savedUser = userService.register(request, UserRole.User);
            savedUser.eraseCredentials();

            URI location = URI.create("/api/users/" + savedUser.getId());
            return ResponseEntity.created(location).body(new UserResponse(savedUser));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/admin/register")
    public ResponseEntity<UserResponse> createAdmin(@RequestBody UserRequest request) {
        try {
            User savedUser = userService.register(request, UserRole.Admin);
            savedUser.eraseCredentials();

            URI location = URI.create("/api/users/" + savedUser.getId());
            return ResponseEntity.created(location).body(new UserResponse(savedUser));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserRequest userRequest, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        userRequest.getUsername(),
                        userRequest.getPassword()));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        securityContextRepository.saveContext(securityContext, request, response);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal User user) {
        return getUser(user, user.getId());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal User user, @PathVariable UUID userId) {
        Optional<User> foundUserOpt = userService.get(user, userId);
        if (foundUserOpt.isEmpty())
            return ResponseEntity.notFound().build();

        User foundUser = foundUserOpt.get();
        foundUser.eraseCredentials();

        return ResponseEntity.ok(new UserResponse(foundUser));
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserByUsername(@AuthenticationPrincipal User user, @RequestParam String username) {
        Optional<User> foundUserOpt = userService.getByUsername(user, username);
        if (foundUserOpt.isEmpty())
            return ResponseEntity.notFound().build();

        User foundUser = foundUserOpt.get();
        foundUser.eraseCredentials();

        return ResponseEntity.ok(new UserResponse(foundUser));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal User user, HttpServletRequest request) {
        return deleteUser(user, user.getId(), request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User user, @PathVariable UUID userId, HttpServletRequest request) {
        boolean wasDeleted = userService.delete(user, userId);

        if (wasDeleted && user.getId().equals(userId)) {
            HttpSession session = request.getSession(false);
            if (session != null)
                session.invalidate();
            SecurityContextHolder.clearContext();
        }

        return wasDeleted
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> changeMe(@AuthenticationPrincipal User user, @RequestBody UserRequest request) {
        return changeUser(user, user.getId(), request);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> changeUser(@AuthenticationPrincipal User user, @PathVariable UUID userId, @RequestBody UserRequest request) {
        try {
            Optional<User> updatedUserOpt = userService.update(user, userId, request);
            if (updatedUserOpt.isEmpty())
                return ResponseEntity.notFound().build();

            User updatedUser = updatedUserOpt.get();
            updatedUser.eraseCredentials();

            return ResponseEntity.ok(new UserResponse(updatedUser));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}

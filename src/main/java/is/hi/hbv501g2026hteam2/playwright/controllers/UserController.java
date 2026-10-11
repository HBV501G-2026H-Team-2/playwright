package is.hi.hbv501g2026hteam2.playwright.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import is.hi.hbv501g2026hteam2.playwright.persistence.enums.UserRole;
import is.hi.hbv501g2026hteam2.playwright.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserController(
            UserService userService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setRole(UserRole.User);

        var hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        User savedUser = userService.register(user);
        savedUser.setPassword(null);

        URI location = URI.create("/api/users/" + savedUser.getId());
        return ResponseEntity.created(location).body(savedUser);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<User> createAdmin(@RequestBody User admin) {
        admin.setRole(UserRole.Admin);

        var hashedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(hashedPassword);

        User savedAdmin = userService.register(admin);
        savedAdmin.setPassword(null);

        URI location = URI.create("/api/users/" + savedAdmin.getId());
        return ResponseEntity.created(location).body(savedAdmin);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
            )
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@AuthenticationPrincipal User user, @PathVariable UUID userId) {
        if (user.getId() != userId && user.getRole() != UserRole.Admin)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return userService.get(userId)
            .map(foundUser -> ResponseEntity.ok(foundUser))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<User> getUserByEmail(@AuthenticationPrincipal User user, @RequestParam String email) {
        if (user.getEmail() != email && user.getRole() != UserRole.Admin)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return userService.getByEmail(email)
            .map(foundUser -> ResponseEntity.ok(foundUser))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User user, @PathVariable UUID userId) {
        if (user.getId() != userId && user.getRole() != UserRole.Admin)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


        return userService.delete(userId)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }

}

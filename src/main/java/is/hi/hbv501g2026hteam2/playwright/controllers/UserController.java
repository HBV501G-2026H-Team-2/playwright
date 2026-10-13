package is.hi.hbv501g2026hteam2.playwright.controllers;

import java.net.URI;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

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

        try {
            User savedUser = userService.register(user);
            savedUser.eraseCredentials();

            URI location = URI.create("/api/users/" + savedUser.getId());
            return ResponseEntity.created(location).body(savedUser);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/admin/register")
    public ResponseEntity<User> createAdmin(@RequestBody User admin) {
        admin.setRole(UserRole.Admin);

        var hashedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(hashedPassword);

        User savedAdmin = userService.register(admin);
        savedAdmin.eraseCredentials();

        URI location = URI.create("/api/users/" + savedAdmin.getId());
        return ResponseEntity.created(location).body(savedAdmin);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
            )
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        securityContextRepository.saveContext(securityContext, request, response);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@AuthenticationPrincipal User user, @PathVariable UUID userId) {
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (!user.getId().equals(userId) && user.getRole() != UserRole.Admin)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return userService.get(userId)
            .map(foundUser -> {
                foundUser.eraseCredentials();
                return ResponseEntity.ok(foundUser);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<User> getUserByUsername(@AuthenticationPrincipal User user, @RequestParam String username) {
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (!user.getUsername().equals(username) && user.getRole() != UserRole.Admin)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return userService.getByUsername(username)
            .map(foundUser -> {
                foundUser.eraseCredentials();
                return ResponseEntity.ok(foundUser);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User user, @PathVariable UUID userId, HttpServletRequest request) {
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (!user.getId().equals(userId) && user.getRole() != UserRole.Admin)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        boolean wasDeleted = userService.delete(userId);

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

}

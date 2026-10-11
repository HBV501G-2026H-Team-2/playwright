package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import is.hi.hbv501g2026hteam2.playwright.persistence.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public final class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private UserRole role;

    private Instant createdAt;

    private Instant updatedAt;

    public User() {
        this.createdAt = this.updatedAt = Instant.now();
    }

    public User(String email, String password) {
        this(email, password, UserRole.User);
    }

    public User(String email, String password, UserRole role) {
        this();
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return email;
    }

    public void setEmail(String email) {
        this.updatedAt = Instant.now();
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.updatedAt = Instant.now();
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    public void setRole(UserRole role) {
        this.updatedAt = Instant.now();
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}

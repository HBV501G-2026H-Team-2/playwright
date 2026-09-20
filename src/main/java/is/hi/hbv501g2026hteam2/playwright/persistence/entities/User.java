package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import java.time.Instant;
import java.util.UUID;

import is.hi.hbv501g2026hteam2.playwright.persistence.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public final class User {
    @Id
    private UUID id;
    private String email;
    private String password;
    private UserRole role;
    private Instant createdAt;
    private Instant updatedAt;

    public User() {
        this.id = UUID.randomUUID();
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
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

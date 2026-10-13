package is.hi.hbv501g2026hteam2.playwright.persistence.entities;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonInclude;

import is.hi.hbv501g2026hteam2.playwright.persistence.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public final class User implements UserDetails, CredentialsContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(nullable = false)
    private String password;

    private UserRole role;

    private Instant createdAt;

    private Instant updatedAt;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Collection<Track> tracks;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Collection<Playlist> playlists;

    public User() {
        this.createdAt = this.updatedAt = Instant.now();
    }

    public User(String username, String password) {
        this(username, password, UserRole.User);
    }

    public User(String username, String password, UserRole role) {
        this();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.updatedAt = Instant.now();
        this.username = username;
    }

    @Override
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

    @Override
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

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

}

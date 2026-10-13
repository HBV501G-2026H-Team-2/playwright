package is.hi.hbv501g2026hteam2.playwright.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetailsService;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;

public interface UserService extends UserDetailsService {

    User register(User user);

    boolean delete(UUID id);

    Optional<User> get(UUID id);

    Optional<User> getByUsername(String username);

    User updateUsername(User user, String newUsername);

    User updatePassword(User user, String newPassword);

}

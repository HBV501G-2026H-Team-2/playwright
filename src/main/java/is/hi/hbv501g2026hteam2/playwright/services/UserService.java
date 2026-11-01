package is.hi.hbv501g2026hteam2.playwright.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetailsService;

import is.hi.hbv501g2026hteam2.playwright.dto.UserRequest;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import is.hi.hbv501g2026hteam2.playwright.persistence.enums.UserRole;

public interface UserService extends UserDetailsService {

    User register(UserRequest request, UserRole role);

    boolean delete(User user, UUID id);

    Optional<User> get(User user, UUID id);

    Optional<User> getByUsername(User user, String username);

    Optional<User> update(User user, UUID id, UserRequest request);

}

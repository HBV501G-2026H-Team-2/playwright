package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import is.hi.hbv501g2026hteam2.playwright.dto.UserRequest;
import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import is.hi.hbv501g2026hteam2.playwright.persistence.enums.UserRole;
import is.hi.hbv501g2026hteam2.playwright.persistence.repositories.UserRepository;
import is.hi.hbv501g2026hteam2.playwright.services.UserService;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplementation(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(UserRequest request, UserRole role) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getUsername(), hashedPassword, role);
        return repository.save(user);
    }

    @Override
    public boolean delete(User user, UUID id) {
        Optional<User> foundUser = get(user, id);
        if (foundUser.isEmpty())
            return false;

        repository.delete(foundUser.get());
        return true;
    }

    @Override
    public Optional<User> get(User user, UUID id) {
        if (user.getId().equals(id))
            return Optional.of(user);
        if (user.isAdmin())
            return repository.findById(id);
        return Optional.empty();
    }

    @Override
    public Optional<User> getByUsername(User user, String username) {
        if (user.getUsername().equals(username))
            return Optional.of(user);
        if (user.isAdmin())
            return repository.findByUsername(username);
        return Optional.empty();
    }

    @Override
    public User loadUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("unknown username"));
    }

    @Override
    public Optional<User> update(User user, UUID id, UserRequest request) {
        Optional<User> foundUserOpt = get(user, id);
        if (foundUserOpt.isEmpty())
            return Optional.empty();

        User foundUser = foundUserOpt.get();

        if (request.getUsername() != null)
            foundUser.setUsername(request.getUsername());

        if (request.getPassword() != null) {
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            foundUser.setPassword(hashedPassword);
        }

        return Optional.of(repository.save(foundUser));
    }

}

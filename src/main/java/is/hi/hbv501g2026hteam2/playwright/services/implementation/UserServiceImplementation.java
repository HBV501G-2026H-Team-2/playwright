package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import is.hi.hbv501g2026hteam2.playwright.persistence.repositories.UserRepository;
import is.hi.hbv501g2026hteam2.playwright.services.UserService;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImplementation(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User register(User user) {
        return repository.save(user);
    }

    @Override
    public boolean delete(UUID id) {
        if (!repository.existsById(id))
            return false;

        repository.deleteById(id);
        return true;
    }

    @Override
    public Optional<User> get(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public User loadUserByUsername(String username) {
        return getByUsername(username).orElseThrow(() -> new UsernameNotFoundException("unknown username"));
    }

    @Override
    public User updateUsername(User user, String newUsername) {
        user.setUsername(newUsername);
        return repository.save(user);
    }

    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return repository.save(user);
    }

}

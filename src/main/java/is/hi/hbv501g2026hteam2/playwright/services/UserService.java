package is.hi.hbv501g2026hteam2.playwright.services;

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import java.util.UUID;

public interface UserService {
    User register(User user); 
    void delete(User user); 
    void login(User user); 
    void logout(User user); 
    User get(UUID id); 
    User getByEmail(String email); 
    User updateEmail(User user, String newEmail); 
    User updatePassword(User user, String newPassword);
}
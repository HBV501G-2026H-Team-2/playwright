package is.hi.hbv501g2026hteam2.playwright.persistence.repositories; 

import is.hi.hbv501g2026hteam2.playwright.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository; 
import java.util.Optional; 
import java.util.UUID; 

public interface UserRepository extends JpaRepository<User, UUID> {
    User save(User user); 
    void delete(User user); 
    Optional<User> findById(UUID id); 
    Optional<User> findByEmail(String email); 
}
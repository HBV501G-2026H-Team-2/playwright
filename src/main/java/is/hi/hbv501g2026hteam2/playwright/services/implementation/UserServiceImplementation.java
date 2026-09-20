package is.hi.hbv501g2026hteam2.playwright.services.implementation;

import is.hi.hbv501g2026hteam2.playwright.services.UserService;

@Service
public class UserServiceImplementation implements UserService {
    private UserRepository repository; 

    @Autowired
    public UserServiceImplementation(UserRepository repository){
        this.repository = repository;
    }

    @Override
    public User register(User user){
        return null; 
    }

    @Override
    public void delete(User user){} 
    
    @Override
    public void login(User user){} 

    @Override
    public void logout(User user){} 

    @Override
    public User get(UUID id){
        return null; 
    } 

    @Override
    public User getByEmail(String email){
        return null; 
    } 
    
    @Override
    public User updateEmail(User user, String newEmail){
        return null; 
    } 

    @Override
    public User updatePassword(User user, String newPassword){
        return null; 
    }
}
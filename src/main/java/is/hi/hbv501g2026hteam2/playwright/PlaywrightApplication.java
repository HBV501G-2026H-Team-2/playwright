package is.hi.hbv501g2026hteam2.playwright;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PlaywrightApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaywrightApplication.class, args);
    }

}

package is.hi.hbv501g2026hteam2.playwright.persistence.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    User {
        public String getAuthority() {
            return "ROLE_USER";
        }
    },
    Admin {
        public String getAuthority() {
            return "ROLE_ADMIN";
        }
    },

}

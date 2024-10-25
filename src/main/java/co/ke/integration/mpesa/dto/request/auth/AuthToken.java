package co.ke.integration.mpesa.dto.request.auth;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
@Getter
public class AuthToken {
    private String token;
    private LocalDateTime expiryTime;

    public boolean isValid() {
        return token != null && LocalDateTime.now().isBefore(expiryTime);
    }

    public void update(String token, long expiresIn) {
        this.token = token;
        this.expiryTime = LocalDateTime.now().plusSeconds(expiresIn);
    }


}

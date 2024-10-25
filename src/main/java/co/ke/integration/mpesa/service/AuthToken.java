package co.ke.integration.mpesa.service;

public class AuthToken {
    private String token;
    private long expiryTime;  // Epoch time when the token expires

    public AuthToken(String token, long expiresIn) {
        this.token = token;
        this.expiryTime = System.currentTimeMillis() + (expiresIn * 1000);  // Convert seconds to milliseconds
    }

    public String getToken() {
        return token;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

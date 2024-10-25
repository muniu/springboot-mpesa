package co.ke.integration.mpesa.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mpesa")
@Getter
@Setter
public class MpesaConfig {
    private String consumerKey;
    private String consumerSecret;
    private String authurl;
    private String balanceurl;


}

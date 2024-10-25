package co.ke.integration.mpesa.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mpesa")
public class MpesaConfig {

    private String consumerkey;
    private String consumerSecret;
    private String authurl;
    private String balanceurl;

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getAuthurl() {
        return consumerkey;
    }

    public void setAuthurl(String authurl) {
        this.authurl = authurl;
    }

    public String getBalanceurl() {
        return balanceurl;
    }

    public void setBalanceurl(String balanceurl) {
        this.balanceurl = balanceurl;
    }
    public String getconsumerKey() {
        return consumerkey;
    }

    public void setconsumerKey(String consumerKey) {
        this.consumerkey = consumerKey;
    }

}

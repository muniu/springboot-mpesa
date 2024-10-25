package co.ke.integration.mpesa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tech.ailef.snapadmin.external.SnapAdminAutoConfiguration;

@SpringBootApplication
@ImportAutoConfiguration(SnapAdminAutoConfiguration.class) //Enable SnapAdmin AutoConfiguration

public class MpesaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpesaApplication.class, args);
	}

}

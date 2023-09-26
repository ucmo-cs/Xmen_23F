package edu.ucmo.cbbackend;

import edu.ucmo.cbbackend.model.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class CbBackendApplication {

    public static void main(String[] args) {
            SpringApplication.run(CbBackendApplication.class, args);
    }

}

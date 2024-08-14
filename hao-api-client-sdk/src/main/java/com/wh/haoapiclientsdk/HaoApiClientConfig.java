package com.wh.haoapiclientsdk;

import com.wh.haoapiclientsdk.client.HaoApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("haoapi.client")
@Data
@ComponentScan
public class HaoApiClientConfig {

    private String accessKey;

    private String secretKey;

   @Bean
   public HaoApiClient yuApiClient() {
       return new HaoApiClient(accessKey, secretKey);
   }

}

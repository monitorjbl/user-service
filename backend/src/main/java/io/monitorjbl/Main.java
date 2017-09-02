package io.monitorjbl;

import io.monitorjbl.model.Crypter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@SpringBootApplication
public class Main {

  @Value("${encryption.key}")
  private String encryptionKey;

  public static void main(String[] args) {
    start(args);
  }

  public static ConfigurableApplicationContext start(String[] args) {
    return SpringApplication.run(Main.class, args);
  }

  @PostConstruct
  public void setEncryptionKey() {
    Crypter.setKey(encryptionKey);
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jacksonJava8Dates() {
    return builder -> builder.featuresToDisable(WRITE_DATES_AS_TIMESTAMPS);
  }

}

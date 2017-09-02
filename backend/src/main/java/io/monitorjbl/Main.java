package io.monitorjbl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@SpringBootApplication
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jacksonJava8Dates() {
    return builder -> builder.featuresToDisable(WRITE_DATES_AS_TIMESTAMPS);
  }

}

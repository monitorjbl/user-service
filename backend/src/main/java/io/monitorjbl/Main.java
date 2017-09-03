package io.monitorjbl;

import io.monitorjbl.model.Crypter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@EnableSwagger2
@SpringBootApplication
public class Main {

  @Value("${encryption.key:archaeoastronomy}")
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

  @Bean
  public Docket productApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("io.monitorjbl"))
        .build();

  }

}

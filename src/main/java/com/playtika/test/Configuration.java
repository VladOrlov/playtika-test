package com.playtika.test;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@Data
@SpringBootConfiguration
@ConfigurationProperties(prefix = "app")
public class Configuration {

  private String scanPath;
  private String savePath;

  @PostConstruct
  public void init() {
    System.out.println(scanPath);
  }

}

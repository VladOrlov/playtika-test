package com.playtika.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements CommandLineRunner {

  @Autowired
  private com.playtika.test.Configuration configuration;
  @Autowired
  private FilePathReaderImperative filePathReaderImperative;
  @Autowired
  private FileWriter fileWriter;

  @Override
  public void run(String... args) throws Exception {

    filePathReaderImperative.read();
  }
}

package com.playtika.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FileWriter {

//  public static final String BASE_PATH = "/home/vorlov/test";
  @Autowired
  private Configuration configuration;

  public void write(Map<String, String> pathes) {
    try (FileChannel channel = (FileChannel) Files.newByteChannel(
        Paths.get(configuration.getSavePath()), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {

      String result = pathes.keySet().stream().collect(Collectors.joining("\n"));

      byte[] bytesResult = result.getBytes();
      ByteBuffer buffer = ByteBuffer.allocate(bytesResult.length);

      buffer.put(bytesResult);
      buffer.rewind();
      channel.write(buffer);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

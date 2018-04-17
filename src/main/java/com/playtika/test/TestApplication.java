package com.playtika.test;

import com.google.common.collect.Lists;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Slf4j
@SpringBootApplication
public class TestApplication {

  public static void main(String[] args) throws IOException {
    SpringApplication.run(TestApplication.class, args);

    FilePathReaderImperative filePathReaderSimple = new FilePathReaderImperative();
    filePathReaderSimple.read();

  }

  private static void readFileExample() {
    try (FileChannel channel = (FileChannel) Files.newByteChannel(
        Paths.get("file.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
      ByteBuffer buffer = ByteBuffer.allocate(64);

      IntStream.range(0, buffer.capacity()).forEach(i -> buffer.put((byte) ('A' + i)));
      buffer.rewind();
      channel.write(buffer);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void hashingExample() {
    List<Integer> listA = Lists.newArrayList(10, 65, 97, 42, 41);
    List<Integer> listB = Lists.newArrayList(97, 42, 65, 10, 40);

    int hashTableSize = listA.size() * 4;
    Integer[] hashTable = new Integer[hashTableSize];

    for (Integer elem : listA) {
      int index = getIndex(elem, hashTableSize);
      System.out.println("Element - " + elem + " index - " + index);
      if (hashTable[index] != null) {
        throw new RuntimeException("Wrong hash calculation!");
      } else {
        hashTable[index] = elem;
      }
    }

    for (Integer elem : listB) {
      int index = getIndex(elem, hashTableSize);
      System.out.println("Element - " + elem + " index - " + index);

      if (!Objects.equals(hashTable[index], elem)) {
        throw new RuntimeException("Arrays aren't match! Wrong element - " + elem);
      }
    }
    System.out.println("Arrays are matched");
  }

  private static int getIndex(Integer i, int length) {
    int hash = Objects.hash(Long.valueOf(i) * 20107771);
    System.out.println("Element - " + i + " hash - " + hash);
    //return hash % length;
    return Math.floorMod(hash, length);
  }

  private static void bloomFilterExample() {
    List<Integer> listA = Lists.newArrayList(10, 65, 97, 42, 40);
    List<Integer> listB = Lists.newArrayList(97, 42, 65, 10, 41);

    BloomFilter<Integer> filter = BloomFilter.create(Funnels.integerFunnel(), listA.size(), 0.01);
    listA.forEach(filter::put);

    if (listA.size() == listB.size() && listB.stream().allMatch(filter::mightContain)) {
      log.info("All elements from list B: {} are in list A: {}", listA, listB);
    } else {
      log.info("Lists A: {} and B: {} aren't match", listA, listB);
    }
  }
}

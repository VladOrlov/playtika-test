package com.playtika.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.nio.file.Files.isDirectory;

public class FilePathReaderSimple {

  public static Map<String, String> pathes = new ConcurrentSkipListMap<>();
  public static final String BASE_PATH = "/home/vorlov/test";

  private ExecutorService executorService;
  private Phaser phaser = new Phaser();

  public void read() {
    executorService = Executors.newCachedThreadPool();
    CompletableFuture.runAsync(() -> crawlingDirectory(Paths.get(BASE_PATH)), executorService);
    pathes.forEach((k, v) -> System.out.println(k));


//    try {
//      Thread.sleep(1);
//      executorService.awaitTermination(5, TimeUnit.SECONDS);
//      executorService.shutdown();

//    int i = phaser.awaitAdvance(1);

//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }

  }

  private Void handleException(Throwable ex) {
    System.out.println(ex.getMessage());
    return null;
  }

  public void crawlingDirectory(Path path) {
    try {
      List<Path> paths = Files.list(Paths.get(path.toAbsolutePath().toString())).collect(Collectors.toList());

      if (paths == null || paths.size() == 0) {
        return;
      }

      CompletableFuture.runAsync(() -> paths.stream()
          .filter(p -> isDirectory(p))
          .forEach(p -> pathes.computeIfAbsent(p.toAbsolutePath().toString(),
              key -> {
                pathes.put(key, "1");
                crawlingDirectory(p);
//                CompletableFuture.runAsync(() -> crawlingDirectory(p), executorService);
                return key;
              })), executorService);

      CompletableFuture.runAsync(() -> paths.stream()
              .filter(p -> !isDirectory(p))
              .forEach(p -> pathes.putIfAbsent(p.toAbsolutePath().toString(), "1"))
          , executorService);


    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

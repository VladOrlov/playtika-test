package com.playtika.test;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.nio.file.Files.isDirectory;

@Component
public class FilePathReaderImperative {

  public static Map<String, String> pathes = new ConcurrentSkipListMap<>();
  public static final String BASE_PATH = "/home/vorlov/test";

  private ExecutorService executorService;

  private List<CompletableFuture<Void>> listOfTasks = Lists.newArrayList();

  public void read() {
    executorService = Executors.newCachedThreadPool();
    CompletableFuture.runAsync(() -> crawlingDirectory(Paths.get(BASE_PATH)), executorService);

    try {
      executorService.awaitTermination(1, TimeUnit.SECONDS);
      executorService.shutdown();
      pathes.forEach((k, v) -> System.out.println(k));

      FileWriter fileWriter = new FileWriter();
      fileWriter.write(pathes);

    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  public void crawlingDirectory(Path path) {
    try {
      List<Path> paths = Files.list(Paths.get(path.toAbsolutePath().toString())).collect(Collectors.toList());

      if (paths == null || paths.size() == 0) {
        return;
      }

      listOfTasks.add(CompletableFuture.runAsync(() -> paths.stream()
          .filter(p -> isDirectory(p))
          .forEach(p -> pathes.computeIfAbsent(p.toAbsolutePath().toString(),
              key -> {
                pathes.put(key, "1");
                crawlingDirectory(p);
                return key;
              })), executorService));

      listOfTasks.add(CompletableFuture.runAsync(() -> paths.stream()
          .filter(p -> !isDirectory(p))
          .forEach(p -> pathes.putIfAbsent(p.toAbsolutePath().toString(), "1")), executorService));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

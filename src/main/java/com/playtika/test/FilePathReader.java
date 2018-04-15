package com.playtika.test;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.nio.file.Files.isDirectory;

public class FilePathReader {

  public static Set<String> pathes = new ConcurrentSkipListSet<>();

  public static final String BASE_PATH = "/home/vorlov/test";

  public List<String> getPathes(String basePath) {

    ExecutorService executorService = Executors.newCachedThreadPool();
    try {
      List<Path> paths = Files.list(Paths.get(BASE_PATH)).collect(Collectors.toList());

      List<Path> directoryPathes = paths.stream()
          .filter(path -> isDirectory(path))
          .collect(Collectors.toList());

      CompletableFuture.runAsync(() -> addFilePathesToPool(paths));

      List<CompletableFuture<List<String>>> collect = directoryPathes.stream()
          .map(path -> CompletableFuture.supplyAsync(() ->
              this.getPathes(path.toAbsolutePath().toString())))
          .collect(Collectors.toList());

    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return Lists.newArrayList();
  }

  private void addFilePathesToPool(List<Path> paths) {
    List<String> filePathes = paths.stream()
        .filter(path -> !isDirectory(path))
        .map(path -> path.toAbsolutePath().toString())
        .collect(Collectors.toList());

    pathes.addAll(filePathes);
  }
}

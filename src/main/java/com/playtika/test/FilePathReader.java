package com.playtika.test;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

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

import static java.lang.Runtime.*;
import static java.nio.file.Files.isDirectory;

public class FilePathReader {

  public static Set<String> pathes = new ConcurrentSkipListSet<>();

  public static final String BASE_PATH = "/home/vorlov/Programs";

  public List<String> getPathes(String basePath) {

    ExecutorService executorService = Executors.newCachedThreadPool();
    executorService.isShutdown()
    try {
      List<Path> paths = Files.list(Paths.get(BASE_PATH)).collect(Collectors.toList());

      List<Path> directoryPathes = paths.stream()
          .filter(path -> isDirectory(path))
          .collect(Collectors.toList());

      CompletableFuture.runAsync(() -> addFilePathesToPool(paths));

      directoryPathes.stream()
          .map(path -> CompletableFuture.supplyAsync(() ->
              this.getPathes(path.toAbsolutePath().toString())))
          .collect(Collectors.toList())

    } catch (IOException e) {
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

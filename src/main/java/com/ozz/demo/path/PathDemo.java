package com.ozz.demo.path;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ozz.demo.security.encrypt.digest.DigestDemo;

public class PathDemo {
  private DigestDemo digestDemo;

  public Path getPath(String path) {
    return Paths.get(path);
  }

  public boolean exists(Path path) {
    return Files.exists(path);
  }

  public boolean isDirectory(Path path) {
    return Files.isDirectory(path);
  }

  public String readFileToString(Path path) throws IOException {
    // 短文本
    byte[] bytes = Files.readAllBytes(path);
    return new String(bytes, StandardCharsets.UTF_8);
    /*
     * // 长文本 InputStream in = Files.newInputStream(path); Reader in = Files.newBufferedReader(path,
     * StandardCharsets.UTF_8);
     */
  }

  public List<String> readLines(Path path) throws IOException {
    return Files.readAllLines(path, StandardCharsets.UTF_8);
  }

  public void writeStringToFile(Path path, String content, boolean append) throws IOException {
    // 短文本
    Files.write(path,
                content.getBytes(StandardCharsets.UTF_8),
                append ? StandardOpenOption.APPEND : StandardOpenOption.WRITE);
    /*
     * // 长文本 OutputStream out = Files.newOutputStream(path, append ? StandardOpenOption.APPEND :
     * StandardOpenOption.WRITE); Writer out = Files.newBufferedWriter(path, append ?
     * StandardOpenOption.APPEND : StandardOpenOption.WRITE);
     */
  }

  public void deleteFiles(Path path) throws IOException {
    Files.delete(path);
  }

  public void copyFilesToFolder(Path source, Path target) throws IOException {
    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
  }
  
  public boolean equals(Path path1, Path path2) throws FileNotFoundException, IOException {
      return StringUtils.equals(digestDemo.digest(path1), digestDemo.digest(path2));
  }

  /**
   * 相对路径
   */
  public void relativize(Path root, Path path) {
    // 获取相对路径
    Path r = root.relativize(path);
    // 解析相对路径
    Path newPath = root.resolve(r);
    if(!newPath.equals(path)) {
      throw new RuntimeException("解析路径错误");
    }
  }

  public String getFileName(Path path) {
    return path.getFileName().toString();
  }
  
  public long getSize(Path path) throws IOException {
    return Files.size(path);
  }
}

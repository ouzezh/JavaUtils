package com.ozz.demo.path;

import lombok.extern.slf4j.Slf4j;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.URL;

@Slf4j
public class ResourcePathUtil {

  public static void main(String[] args) {
    log.info(getHomeDirectory());
    log.info(getWorkspacePath());
    log.info(getProjectPath());
    log.info(getResourcePath());
  }

  public static String getHomeDirectory() {
    return FileSystemView.getFileSystemView().getHomeDirectory().getPath();
  }

  public static String getResourcePath() {
    return getResourcePath("/");
  }

  public static String getResourcePath(String path) {
    URL url = ResourcePathUtil.class.getResource(path);
    if (url == null) {
      throw new RuntimeException("未找到路径: " + path);
    } else {
      return new File(url.getPath()).getPath();
    }
  }

  public static String getProjectPath() {
    return System.getProperty("user.dir");
  }

  public static String getWorkspacePath() {
    return new File(getProjectPath()).getParent();
  }

}

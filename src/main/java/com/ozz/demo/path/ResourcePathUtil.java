package com.ozz.demo.path;

import cn.hutool.log.StaticLog;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.URL;

public class ResourcePathUtil {

  public static void main(String[] args) {
    StaticLog.info(getHomeDirectory());
    StaticLog.info(getWorkspacePath());
    StaticLog.info(getProjectPath());
    StaticLog.info(getResourcePath());
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

package com.ozz.demo.path;

import java.net.URL;

/**
 * 
 * 
 * @author ozz
 */
public class ResourcePathUtil {

  public static String getResourcePath() {
    return getResourcePath("/");
  }

  public static String getResourcePath(String path) {
    URL url = ResourcePathUtil.class.getResource(path);
    if (url == null) {
      throw new RuntimeException("未找到路径: " + path);
    } else {
      return url.getPath().replaceFirst("^/([A-Z]+:)", "$1").replaceFirst("/$", "");
    }
  }

  public static String getProjectPath() {
    return System.getProperty("user.dir").replaceAll("\\\\", "/");
  }

  public static String getWorkspacePath() {
    return getProjectPath().replaceFirst("/[^/]+$", "");
  }

}

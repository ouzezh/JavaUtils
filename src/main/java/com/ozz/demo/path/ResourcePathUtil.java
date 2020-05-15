package com.ozz.demo.path;

import java.net.URL;

/**
 * 
 * 
 * @author ozz
 */
public class ResourcePathUtil {

  public String getResourcePath() {
    return getResourcePath("/");
  }

  public String getResourcePath(String path) {
    URL url = getClass().getResource(path);
    if (url == null) {
      throw new RuntimeException("未找到路径: " + path);
    } else {
      return url.getPath().replaceFirst("^/([A-Z]+:)", "$1").replaceFirst("/$", "");
    }
  }

  public String getProjectPath() {
    return System.getProperty("user.dir").replaceAll("\\\\", "/");
  }

  public String getWorkspacePath() {
    return getProjectPath().replaceFirst("/[^/]+$", "");
  }

}

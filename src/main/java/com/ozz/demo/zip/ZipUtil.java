package com.ozz.demo.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {
  private final static String DEFAULT_ENCODING = "gb2312";

  public static void zipFiles(File zipFile, File[] files) throws IOException {
    for (File file : files) {
      if (!file.exists()) {
        throw new FileNotFoundException(file.getPath());
      }
    }

    try (OutputStream fo = new FileOutputStream(zipFile); ZipOutputStream out = new ZipOutputStream(fo);) {
      out.setEncoding(DEFAULT_ENCODING);

      zipFiles(zipFile, files, out, "");
    }
  }

  private static void zipFiles(File zipFile, File[] files, ZipOutputStream out, String zipSubFolder) throws IOException {
    InputStream in;
    String filePath;
    ZipEntry entry;
    for (File file : files) {
      if (file.equals(zipFile)) {
        continue;
      }

      filePath = zipSubFolder + file.getName() + (file.isDirectory() ? File.separator : "");
      entry = new ZipEntry(filePath);
      out.putNextEntry(entry);
      if (file.isDirectory()) {
        zipFiles(zipFile, file.listFiles(), out, filePath);
      } else {
        in = new FileInputStream(file);
        IOUtils.copy(in, out);
        in.close();
      }
    }
  }

  public static void extractFiles(File zipFile, String destDir, boolean overrideExists) throws IOException {
    if (!zipFile.exists()) {
      throw new RuntimeException("FileNotFound: " + zipFile.getPath());
    }

    try (ZipFile zf = new ZipFile(zipFile, DEFAULT_ENCODING);) {
      Enumeration<ZipEntry> en = zf.getEntries();
      while (en.hasMoreElements()) {
        ZipEntry ze = en.nextElement();
        File file = new File(destDir + File.separator + ze.getName());

        if (ze.isDirectory()) {
          if (!file.exists()) {
            file.mkdirs();
          }
        } else {
          if (file.exists() && !overrideExists) {
            throw new RuntimeException("要解压的文件已存在: " + file.getPath());
          }
          if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
          }

          try (OutputStream output = new FileOutputStream(file); InputStream input = zf.getInputStream(ze);) {
            IOUtils.copy(input, output);
          }
        }
      }
    }
  }

  public static String gzip(String primStr) {
    if (primStr == null || primStr.isEmpty()) {
      return primStr;
    }
    try (ByteArrayOutputStream out = new ByteArrayOutputStream(); GZIPOutputStream gzip = new GZIPOutputStream(out)) {
      gzip.write(primStr.getBytes());
      gzip.finish();
      return Base64.getEncoder().encodeToString(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String gunzip(String compressedStr) {
    if (compressedStr == null || compressedStr.isEmpty()) {
      return compressedStr;
    }
    try (ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(compressedStr));
        GZIPInputStream ginzip = new GZIPInputStream(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[1024];
      int offset = -1;
      while ((offset = ginzip.read(buffer)) != -1) {
        out.write(buffer, 0, offset);
      }
      return out.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

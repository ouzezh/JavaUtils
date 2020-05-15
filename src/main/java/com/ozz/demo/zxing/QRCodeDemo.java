package com.ozz.demo.zxing;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class QRCodeDemo {
  public static void main(String[] args) {
    String contents = "https://www.baidu.com";
    String filePath = FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/QRCode.png";

    createQRCode(contents, filePath, 200, 200);
    System.out.println("save QRCode to " + filePath);
  }

  public static void createQRCode(String contents, String filePath, int height, int width) {
    try {
      BitMatrix matrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height);
      try (FileOutputStream out = new FileOutputStream(filePath)) {
        MatrixToImageWriter.writeToStream(matrix, "png", out);
      }
    } catch (WriterException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}

package com.ozz.demo.commons.csv;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.ozz.demo.path.ResourcePathUtil;

public class CSVDemo {
  private ResourcePathUtil resourcePathUtil;

  public void main(String[] args) throws FileNotFoundException, IOException {
    new CSVDemo().writeCsv();
    new CSVDemo().readCsv();
  }

  public void readCsv() {
    final String FILE_NAME = resourcePathUtil.getProjectPath() + "/logs/student.csv";
    final String[] FILE_HEADER = {"Id", "Name"};

    // 显式地配置一下CSV文件的Header，然后设置跳过Header（要不然读的时候会把头也当成一条记录）
    CSVFormat format = CSVFormat.EXCEL.withHeader(FILE_HEADER).withSkipHeaderRecord();

    // 读出数据的代码
    try (InputStream in = new FileInputStream(FILE_NAME); Reader reader = new InputStreamReader(in, "gbk")) {
      Iterable<CSVRecord> records = format.parse(reader);
      for (CSVRecord record : records) {
        record.forEach(t -> {
          System.out.println(t + " ");
        });
      }
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void writeCsv() {
    try {
      Path path = Paths.get(resourcePathUtil.getProjectPath() + "/logs", "student.csv");
      Files.deleteIfExists(path);
      Files.createFile(path);

      // 显式地配置CSV文件的Header
      String[] header = {"Id", "Name"};
      CSVFormat format = CSVFormat.EXCEL.withHeader(header);
      try (BufferedWriter out = Files.newBufferedWriter(path, Charset.forName("UTF-8"));CSVPrinter printer = new CSVPrinter(out, format)) {
        // 处理乱码问题
        String bomStr = new String(new byte[] {(byte) 0xef, (byte) 0xbb, (byte) 0xbf}, "UTF-8");
        out.write(bomStr);

        // 输出数据
        String[][] students = new String[][] {{"001", "谭振宇"}, {"002", "周杰伦"}};
        // 第一行
        String[] student = students[0];
        printer.print(student[0]);
        printer.print(student[1]);
        printer.println();
        student = students[1];
        // 第二行
        List<String> records = new ArrayList<>();
        records.add(student[0]);
        records.add(student[1]);
        printer.printRecord(records);
      }
    }  catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

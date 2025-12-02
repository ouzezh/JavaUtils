package com.ozz.demo.text;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringFormat {
  public static void main(String[] args) {
    log.info(String.format("%-8s: %s", "string", "1"));
    log.info(String.format("%-8s: %08d", "integer", 1));
    log.info(String.format("%-8s: %.2f %.2f", "float", 0.123, 0.1));
    log.info(String.format("%-8s: %,d, %,.2f", "group digit", 1234, 1234.123));
  }
}

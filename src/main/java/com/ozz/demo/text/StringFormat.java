package com.ozz.demo.text;

public class StringFormat {
  public static void main(String[] args) {
    System.out.println(String.format("%-8s: %s", "string", "1"));
    System.out.println(String.format("%-8s: %08d", "integer", 1));
    System.out.println(String.format("%-8s: %.2f", "float", 0.123));
    System.out.println(String.format("%-8s: %,d, %,.2f", "group digit", 1234, 1234.123));
  }
}

package com.ozz.demo.commons.lambda;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import org.apache.commons.lang3.StringUtils;

public class LambdaDemo {

  public static void main(String[] args) throws InterruptedException {
    testLambda();
    testFunctionalInterface();
  }

  private static void testLambda() {
    Arrays.asList("a", "b").forEach(e -> System.out.println(e));
  }

  private static void testFunctionalInterface() {
    // Supplier：无参数，有返回值
    Supplier<String> s = () -> "test Supplier";
    System.out.println(s.get());

    // Consumer：有参数，无返回值
    Consumer<String> c = x -> System.out.println(x);
    c.accept("test Consumer");
    BiConsumer<String, String> bc = (x,y) -> System.out.println(String.format("%s %s", x, y));
    bc.accept("test","BiConsumer");

    // Predicate：断言
    Predicate<Integer> p = x -> Optional.ofNullable(x).orElse(0).intValue() == 0;
    Predicate<Integer> p2 = p.and(Predicate.isEqual(1));
    System.out.println("test Predicate: " + p2.test(1));
    // BiPredicate: Binary Predicate
    BiPredicate<Integer, Integer> bp =(x, y) -> x > y;
    System.out.println("test BiPredicate: " + bp.test(1,0));

    // Function
    Function<String, Integer> f = x -> Optional.ofNullable(x).orElseGet(() -> StringUtils.EMPTY)
        .length();
    System.out.println("test Function: " + f.apply("x"));
    // Function.andThen
    Function<String, String> t = f.andThen(x -> x.toString());
    System.out.println("test Function.andThen: " + t.apply("xx"));
    // Function.compose
    Function<String, Integer> t2 = f.compose(String::toUpperCase);
    System.out.println("test Function.compose: " + t2.apply("xx"));

    // BiFunction: Binary Function
    BiFunction<String, String, String> f2 = (x, y) -> String.format("%s %s", x, y);
    System.out.println(f2.apply("test", "BiFunction"));

    // 一元函数
    UnaryOperator<String> uo = x -> x.toUpperCase();
    System.out.println(uo.apply("test UnaryOperator"));
    // 二元函数
    BinaryOperator<String> bo = (x, y) -> String.format("%s %s", x, y);
    System.out.println(bo.apply("test", "BinaryOperator"));
  }
}

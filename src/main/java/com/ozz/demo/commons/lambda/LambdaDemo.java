package com.ozz.demo.commons.lambda;

import cn.hutool.log.StaticLog;
import com.alibaba.excel.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;

public class LambdaDemo {

  public static void main(String[] args) {
    testLambda();
    testFunctionalInterface();
  }

  private static void testLambda() {
    Arrays.asList("a", "b").forEach(e -> StaticLog.info(e));
  }

  private static void testFunctionalInterface() {
    // Supplier：无参数，有返回值
    Supplier<String> s = () -> "test Supplier";
    StaticLog.info(s.get());

    // Consumer：有参数，无返回值
    Consumer<String> c = x -> StaticLog.info(x);
    c.accept("test Consumer");
    BiConsumer<String, String> bc = (x,y) -> StaticLog.info(String.format("%s %s", x, y));
    bc.accept("test","BiConsumer");

    // Predicate：断言
    Predicate<Integer> p = x -> Optional.ofNullable(x).orElse(0).intValue() == 0;
    Predicate<Integer> p2 = p.and(Predicate.isEqual(1));
    StaticLog.info("test Predicate: " + p2.test(1));
    // BiPredicate: Binary Predicate
    BiPredicate<Integer, Integer> bp =(x, y) -> x > y;
    StaticLog.info("test BiPredicate: " + bp.test(1,0));

    // Function
    Function<String, Integer> f = x -> Optional.ofNullable(x).orElseGet(() -> StringUtils.EMPTY)
        .length();
    StaticLog.info("test Function: " + f.apply("x"));
    // Function.andThen
    Function<String, String> t = f.andThen(x -> x.toString());
    StaticLog.info("test Function.andThen: " + t.apply("xx"));
    // Function.compose
    Function<String, Integer> t2 = f.compose(String::toUpperCase);
    StaticLog.info("test Function.compose: " + t2.apply("xx"));

    // BiFunction: Binary Function
    BiFunction<String, String, String> f2 = (x, y) -> String.format("%s %s", x, y);
    StaticLog.info(f2.apply("test", "BiFunction"));

    // 一元函数
    UnaryOperator<String> uo = x -> x.toUpperCase();
    StaticLog.info(uo.apply("test UnaryOperator"));
    // 二元函数
    BinaryOperator<String> bo = (x, y) -> String.format("%s %s", x, y);
    StaticLog.info(bo.apply("test", "BinaryOperator"));
  }
}

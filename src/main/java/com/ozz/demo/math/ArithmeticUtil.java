package com.ozz.demo.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.Assert;

/**
 * 计算步骤： 1.中辍表达式转后缀 2.计算后缀表达式
 */
public class ArithmeticUtil {

  /**
   * 检测一个字符串是不是数字
   **/
  private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

  /**
   * 运算符优先级
   **/
  private static Map<String, Integer> priority = new HashMap<>();

  static {
    priority.put("*", 2);
    priority.put("/", 2);
    priority.put("+", 1);
    priority.put("-", 1);
  }

  /**
   * 替换表达式中的中文括号
   **/
  @SuppressWarnings("serial")
  private static Map<String, String> replace = new HashMap<String, String>() {
    {
      put("（", "(");
      put("）", ")");
    }
  };

  /**
   * 支持的运算符
   **/
  private static String[] operator = {"+", "-", "*", "/"};

  public static void main(String[] args) {
    String expression = "-2.5+(-1+3)*3-1/2+(1/3+1)*3";
    double res = execute(expression);
    System.out.println(res);
    Assert.state(res == 7, "result error");
  }

  public static double execute(String expression) {
    return execute(expression, 32);
  }

  /**
   * 整个计算过程
   *
   * @param expression 传入的表达式
   * @return 计算结果
   */
  public static double execute(String expression, int scale) {
    return suffixToValue(inffixToSuffix(trim(expression)), scale);
  }

  /**
   * 整理算式，替换掉中文括号等
   *
   * @param expression 算式
   * @return 整理后的算式
   */
  private static String trim(String expression) {
    if (expression.matches(".*[0-9\\.]\\s[0-9\\.].*")) {
      throw new ArithmeticException(String.format("invalid expression '%s'", expression));
    }
    String result = expression.replaceAll("\\s", "");
    for (Map.Entry<String, String> entry : replace.entrySet()) {
      result = result.replace(entry.getKey(), entry.getValue());
    }
    return result;
  }

  /**
   * 将中缀表达式转换成后缀表达式 eg：中缀表达式8+(9-1）*8+7/2 后缀表达式8 9 1 - 8 * + 7 2 / +，元素之间之间用空格分隔。
   * 从左到右遍历中缀表达式的每一个数字和运算符 如果数字就输出（即存入后缀表达式） 如果是右括号，则弹出左括号之前的运算符 如果优先级低于栈顶运算符，则弹出栈顶运算符，并将当前运算符进栈
   * 遍历结束后，将栈则剩余运算符弹出。
   *
   * @param expression 中缀表达式
   * @return 后缀表达式
   */
  private static String inffixToSuffix(String expression) {
    Stack<String> operatorStack = new Stack<>();
    StringBuilder inffix = new StringBuilder(expression);
    StringBuilder suffix = new StringBuilder();
    String element = null; // 中缀表达式的数字或者运算符
    while (inffix.length() > 0) {
      element = popNextElement(inffix, element);
      if (isNum(element)) { // 是数字则输出
        suffix.append(element).append(" ");
      } else if ("(".equals(element)) {
        operatorStack.push(element);
      } else if (")".equals(element)) { // 右括号则将左括号之前的内容全弹出
        String tmp = operatorStack.pop();
        while (!"(".equals(tmp)) {
          suffix.append(tmp).append(" ");
          tmp = operatorStack.pop();
        }
      } else {
        while (isPop(operatorStack, element)) {// 优先级小于等于栈顶运算符，则弹出
          String tmp = operatorStack.pop();
          suffix.append(tmp).append(" ");
        }
        operatorStack.push(element);
      }
    }

    // 把栈中剩余运算符都弹出
    while (operatorStack.size() > 0) {
      suffix.append(operatorStack.pop()).append(" ");
    }

    return suffix.toString();
  }

  private static boolean isPop(Stack<String> stack, String element) {
    String op = getTopOperator(stack);
    if (op == null) {
      return false;
    }
    return priority.get(op).intValue() - getPriority(element) >= 0;
  }

  private static int getPriority(String element) {
    Integer p = priority.get(element);
    if (p == null) {
      throw new ArithmeticException(String.format("invalid char '%s'", element));
    }
    return p;
  }

  /**
   * 根据后缀表达式算出结果 eg：中缀表达式8+(9-1）*8+7/2 后缀表达式8 9 1 - 8 * + 7 2 / +，元素之间之间用空格分隔。 从左到右遍历后缀表达式 遇到数字就进栈
   * 遇到符号，就将栈顶的两个数字出栈运算，运算结果进栈，直到获得最终结果。 -----------目前只支持整数，由于整数相除会出现浮点数，这里用String作为返回值--------
   *
   * @param expression 后缀表达式
   * @return 结果
   */
  private static double suffixToValue(String expression, int scale) {
    // 已经用空格分隔，直接分割
    String[] suffix = expression.split(" ");
    Stack<Object> stack = new Stack<>();
    BigDecimal num1 = BigDecimal.valueOf(0), num2 = BigDecimal
        .valueOf(0); // 注意次序，num2在栈顶，整数运算结果也可能是double

    for (int i = 0; i < suffix.length; i++) {
      if (isNum(suffix[i])) { // 数字
        stack.push(new BigDecimal(suffix[i]));
      } else { // 是操作符
        num2 = (BigDecimal) stack.pop();
        num1 = (BigDecimal) stack.pop();
        stack.push(calculate(num1, num2, suffix[i], scale));
      }
    }

    // 最终结果也压在栈中，取出即可
    return ((BigDecimal) stack.pop()).doubleValue();
  }

  /**
   * 目前只支持整数，但是整数运算结果也可能是double，故这里传入的参数用double。
   *
   * @param num1     第一个数，在前
   * @param num2     第二个数，在后
   * @param operator 运算符
   * @return 运算结果，除数为0则返回Error
   */
  private static BigDecimal calculate(BigDecimal num1, BigDecimal num2, String operator,
      int scale) {
    BigDecimal result;
    switch (operator) {
      case "+":
        result = num1.add(num2);
        break;
      case "-":
        result = num1.subtract(num2);
        break;
      case "*":
        result = num1.multiply(num2);
        break;
      case "/":
        try {
          result = num1.divide(num2, scale, RoundingMode.HALF_UP);
        } catch (RuntimeException e) {
          throw e;
        }
        break;
      default:
        throw new ArithmeticException(String.format("operator %s not support", operator));
    }
    return result;
  }

  /**
   * 检测字符是不是数字 浮点数其实可以写零宽断言，但是
   *
   * @param c 待检测字符
   * @return 检测结果
   */
  private static boolean isNum(char c) {
    if ((c >= '0' && c <= '9') || c == '.') {
      return true;
    }
    return false;
  }

  /**
   * 检测字符串是不是数字
   *
   * @param str 待检测字符串
   * @return 检测结果
   */
  private static boolean isNum(String str) {
    Matcher matcher = pattern.matcher(str);
    if (matcher.matches()) {
      return true;
    }
    return false;
  }

  /**
   * 获取栈顶运算符
   *
   * @return 如果栈中无运算符，则返回空字符串
   */
  private static String getTopOperator(Stack<String> stack) {
    if (stack.isEmpty()) {
      return null;
    }
    String tmp = stack.lastElement();
    if ("(".equals(tmp)) {
      tmp = null;
    } else if (isOperator(tmp)) {
    } else {
      throw new ArithmeticException(String.format("invalid operator '%s'", tmp));
    }
    return tmp;
  }

  /**
   * 检测一个字符是不是运算符 栈中不是运算符就是括号
   *
   * @param str 待检测字符，由于可能有多位数字字符串，这里用的是String
   * @return 检测结果
   */
  private static boolean isOperator(String str) {
    for (int i = 0; i < operator.length; i++) {
      if (operator[i].equals(str)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 获取表达式的下一个运算符或数字，同时从表达式中删除该元素
   *
   * @param expression 表达式
   * @return
   */
  private static String popNextElement(StringBuilder expression, String previousElement) {
    StringBuilder result = new StringBuilder();
    char c = expression.charAt(0);
    expression.deleteCharAt(0);
    result.append(c);

    if (isNum(c) || ('-' == c && (previousElement == null || "(".equals(previousElement)))) {
      // 如果第一次取到的是数字，则继续检查
      while (expression.length() > 0 && isNum(c = expression.charAt(0))) {
        expression.deleteCharAt(0);
        result.append(c);
      }
    }

    return result.toString();
  }
}

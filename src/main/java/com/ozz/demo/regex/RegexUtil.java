package com.ozz.demo.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 
 * @author ozz
 */
public class RegexUtil {

  private String REGEX_YMD = "((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])" + "|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                                    + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?"
                                    + "((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                                    + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

  private final Pattern PATTERN_DATA_YMD = Pattern.compile("^" + REGEX_YMD + "$");
  private final Pattern PATTERN_DATA_FULL = Pattern.compile("^(" + REGEX_YMD + ") (2[0-3]|[0-1]\\d):[0-5]\\d:[0-5]\\d$");
  private final Pattern PATTERN_VARIABLE = Pattern.compile("^[A-Za-z][A-Za-z_0-9]*$");

  public void replaceWithCapturingGroup() {
    System.out.println("a<b>c".replaceFirst("<(.*)>", "$1"));
  }

  public boolean isValidDateYMD(String date) {
    return regexFind(PATTERN_DATA_YMD, date);
  }

  public boolean isValidDateFull(String date) {
    return regexFind(PATTERN_DATA_FULL, date);
  }

  public boolean isValidVariable(String name) {
    return regexFind(PATTERN_VARIABLE, name);
  }

  private boolean regexFind(Pattern pattern, String str) {
    if (StringUtils.isNotEmpty(str)) {
      Matcher m = pattern.matcher(str);
      if (m.find()) {
        return true;
      }
    }
    return false;
  }

  public Matcher regexFind(String regex, String str) {
    if (StringUtils.isEmpty(str)) {
      str = "";
    }
    Pattern pattern = Pattern.compile(regex);
    Matcher m = pattern.matcher(str);
    return m;
  }

}

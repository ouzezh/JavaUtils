package com.ozz.demo.beanutils;

import org.springframework.beans.BeanUtils;

public class BeanUtilDemo {
  public void copyProperties(Object source, Object target) {
    BeanUtils.copyProperties(source, target);
  }

  /**
   * @param editable 只拷贝只类上有的属性
   */
  public void copyProperties(Object source, Object target, Class<?> editable) {
    BeanUtils.copyProperties(source, target, editable);
  }

  public void copyProperties(Object source, Object target, String... ignoreProperties) {
    BeanUtils.copyProperties(source, target, ignoreProperties);
  }
}

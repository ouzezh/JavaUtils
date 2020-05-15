package com.ozz.demo.beanutils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

public class BeanUtil {
  public interface ICloneable extends Cloneable {
    public ICloneable clone();
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

  @SuppressWarnings("unchecked")
  public static <T extends ICloneable> List<T> cloneList(List<T> tmpList, Class<T> clz) {
    if(tmpList == null) {
      return null;
    }
    List<T> list = new ArrayList<>();
    for(T tmp : tmpList) {
      list.add((T) tmp.clone());
    }
    return null;
  }
}

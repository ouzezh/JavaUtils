package com.ozz.demo.json;

import cn.hutool.log.StaticLog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ozz.demo.json.model.Item;
import com.ozz.demo.json.model.Page;
import java.util.List;

public class JsonUtil {

  public static void main(String[] args) {
    // 链接
    String rowsJson =
        "[{\"id\":\"id1\",\"name\":\"name1\",\"date\":\"2018-08-08 08:08\",\"localDateTime\":\"2019-09-09 09:09:09\"}]";
    List<Item> rows = parseObject(rowsJson, new TypeReference<List<Item>>() {
    });
    // 链接2
    rows = parseArray(rowsJson, Item.class);
    StaticLog.info(JsonUtil.toJSONString(rows));
    // 复杂对象
    Page<Item> page = parseObject("{\"rows\":" + rowsJson + "}", new TypeReference<Page<Item>>() {
    });
    StaticLog.info(JsonUtil.toJSONString(page));
  }

  public static String toJSONString(Object object) {
//    JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);//是否输出值为null的字段,默认为false
    return JSON.toJSONString(object);
  }

  public static <T> T parseObject(String text, Class<T> clazz) {
    return JSON.parseObject(text, clazz);
  }

  /**
   * 解析复杂格式
   */
  public static <T> T parseObject(String text, TypeReference<T> typeReference) {
    return JSON.parseObject(text, typeReference);
  }

  public static <T> List<T> parseArray(String text, Class<T> clazz) {
    return JSON.parseArray(text, clazz);
  }
}

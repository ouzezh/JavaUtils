package com.ozz.demo.json;

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
    List<Item> rows = fromJson(rowsJson, new TypeReference<List<Item>>() {
    });
    System.out.println(JsonUtil.toJson(rows));
    // 复杂对象
    Page<Item> page = fromJson("{\"rows\":" + rowsJson + "}", new TypeReference<Page<Item>>() {
    });
    System.out.println(JsonUtil.toJson(page));
  }

  public static String toJson(Object object) {
//    return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
    return JSON.toJSONString(object);
  }

  public static <T> T fromJson(String text, Class<T> clazz) {
    return JSON.parseObject(text, clazz);
  }

  /**
   * 解析复杂格式
   */
  public static <T> T fromJson(String text, TypeReference<T> typeReference) {
    return JSON.parseObject(text, typeReference);
  }
}

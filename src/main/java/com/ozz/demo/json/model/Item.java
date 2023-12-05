package com.ozz.demo.json.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
// chain:set返回当前对象, prefix:get/set忽略属性前缀, fluent: get/set使用基础属性名且set返回当前对象
@Accessors(chain = true)
public class Item implements BaseObject {

  private String id;
  private String name;
  private String testNull;
  //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  @JSONField(format = "yyyy-MM-dd HH:mm")
  private Date date;
  //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime localDateTime;

  //  @JsonIgnore
  @JSONField(serialize = false)
  public String getTime() {
    return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
  }
}



package com.ozz.demo.json.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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



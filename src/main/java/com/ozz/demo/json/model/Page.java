package com.ozz.demo.json.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Page<T> implements BaseObject {
  private List<T> rows;
}

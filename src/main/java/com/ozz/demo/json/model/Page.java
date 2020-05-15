package com.ozz.demo.json.model;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class Page<T> implements BaseObject {

  private List<T> rows;
}

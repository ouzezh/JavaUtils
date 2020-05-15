package com.ozz.demo.text.template;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.hubspot.jinjava.Jinjava;

public class JinjavaDemo {
  Jinjava jinjava = new Jinjava();

  public static void main(String[] args) throws IOException {
    Map<String, Object> bindings = Maps.newHashMap();
    bindings.put("obj", Collections.singletonMap("name", "Cook"));
    bindings.put("list", ImmutableList.of(Pair.of("list1", "l1"), Pair.of("list2", "l2")));
    bindings.put("values", ImmutableList.of("v1", "v2"));
    System.out.println(new JinjavaDemo().renderTemplate("my-template.properties", bindings));
  }

  public String renderTemplate(String resourceName, Map<String, ?> bindings) {
    try {
      String template = Resources.toString(Resources.getResource(getClass(), resourceName), Charsets.UTF_8);
      String renderedTemplate = jinjava.render(template, bindings);
      return renderedTemplate;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

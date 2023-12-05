package com.ozz.demo.commons.csv;

import cn.hutool.core.annotation.Alias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyRecord {
    private String id;
    @Alias("姓名")
    private String name;
}

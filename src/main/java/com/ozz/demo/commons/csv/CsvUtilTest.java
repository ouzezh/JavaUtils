package com.ozz.demo.commons.csv;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.ozz.demo.path.ResourcePathUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvUtilTest {
    public static void main(String[] args) {
        String name = ResourcePathUtil.getProjectPath() + "/logs/test.csv";
        ArrayList<MyRecord> list = ListUtil.toList(new MyRecord("001", "鸡蛋"), new MyRecord("002", "milk"));

        // 写入
        CsvUtils.writeCsv(name, MyRecord.class, list);

        // 读取
        List<MyRecord> l = CsvUtils.readCsv(name, MyRecord.class, StandardCharsets.UTF_8);
        log.info(JSON.toJSONString(l));
    }
}

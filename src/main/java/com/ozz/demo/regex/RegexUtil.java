package com.ozz.demo.regex;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReUtil;

import java.util.ArrayList;
import java.util.List;

public class RegexUtil {
    public static void main(String[] args) {
        String content = "ZZZaaabbbccc中文1234";

        //抽取多个分组然后把它们拼接起来
        String resultExtractMulti = ReUtil.extractMulti("(\\w)aa(\\w)", content, "$1-$2");
        Assert.isTrue("Z-a".equals(resultExtractMulti));

        //删除第一个匹配到的内容
        String resultDelFirst = ReUtil.delFirst("(\\w)aa(\\w)", content);
        Assert.isTrue("ZZbbbccc中文1234".equals(resultDelFirst));

        //查找所有匹配文本
        List<String> resultFindAll = ReUtil.findAll("\\w{2}", content, 0, new ArrayList<String>());

        //给定字符串是否匹配给定正则
        boolean isMatch = ReUtil.isMatch("\\w+[\u4E00-\u9FFF]+\\d+", content);
        Assert.isTrue(isMatch);

        // 通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串
        //此处把1234替换为 ->1234<-
        String replaceAll = ReUtil.replaceAll(content, "(\\d+)", "->$1<-");
        Assert.isTrue("ZZZaaabbbccc中文->1234<-".equals(replaceAll));
    }
}

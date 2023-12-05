package com.ozz.demo.httpclient;

import cn.hutool.core.io.resource.BytesResource;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;

import java.io.*;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtils {
    public static void main(String[] args) {
        Map<String, String> header = Collections.singletonMap("testHeader", "text/plain");
        Map<String, String> cookie = Collections.singletonMap("testCookie", "xx");

        StaticLog.info(get("https://www.baidu.com?p=v", Collections.singletonMap("testP", "testV"), header, cookie));

        StaticLog.info(post("https://www.baidu.com", "{\"myBody\":0}", header, cookie));

//        upload("http://localhost:8080/v1/test/upload",
//                new File("C:\\Users\\ouzezhou\\Desktop\\Temp\\202108\\intellij-java-google-style.xml"));
//
//        download("http://localhost:8080/v1/test/download");
    }

    public static String get(String url, Map<String, Object> paramMap, Map<String, String> headers,
                             Map<String, String> cookies) {
//        return HttpUtil.get(url);
        HttpRequest req = setParam(HttpUtil.createGet(url), paramMap, null, headers, cookies);
        return doRequest(req);
    }

    public static String post(String url, String body, Map<String, String> headers, Map<String, String> cookies) {
        HttpRequest req = HttpUtil.createPost(url);
        setParam(req, null, body, headers, cookies);
        return doRequest(req);
    }

    /**
     * 上传文件
     */
    public static void upload(String url, File file) {
        HashMap<String, Object> paramMap = new HashMap<>();
        //文件上传只需将参数中的键指定（默认file），值设为文件对象即可，对于使用者来说，文件上传与普通表单提交并无区别
        paramMap.put("file", file);
        String result = HttpUtil.post(url, paramMap);
        System.out.println(result);
    }

    public static void upload(String url, byte[] data, String fileName) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("file", new BytesResource(data, fileName));
        HttpResponse result = HttpUtil.createPost(url).form(paramMap).execute();
        Assert.isTrue(result.isOk());
        System.out.println(result.body());
    }

    /**
     * 下载文件
     */
    @SneakyThrows
    public static void download(String url) {
//        HttpUtil.createGet(url).execute().bodyStream();
//        long size = HttpUtil.downloadFile(url, FileUtil.file("C:/"));
        String fileStr = HttpUtil.downloadString(url, StandardCharsets.UTF_8);
        StaticLog.info("Download file: " + fileStr);
    }

    private static HttpRequest setParam(HttpRequest req, Map<String,Object> paramMap, String body, Map<String,String> headers, Map<String,String> cookies) {
        if(paramMap != null) {
            req.form(paramMap);
        }
        if(body != null) {
            req.body(body);
        }
        if(headers != null) {
            for (Map.Entry<String, String> en : headers.entrySet()) {
                req.header(en.getKey(), en.getValue());
            }
        }
        if(cookies != null) {
            req.cookie(cookies.entrySet().stream().map(t -> new HttpCookie(t.getKey(), t.getValue())).collect(Collectors.toList()));
        }
        return req;
    }

    private static String doRequest(HttpRequest req) {
        HttpResponse resp = req.execute();
        if (resp.getStatus() >= 300) {
            throw new RuntimeException(StrUtil.format("{} - {}", resp.getStatus(), resp.toString()));
        } else {
            return resp.body();
        }
    }

}

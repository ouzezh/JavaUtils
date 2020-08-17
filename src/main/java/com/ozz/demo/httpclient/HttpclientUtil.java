package com.ozz.demo.httpclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class HttpclientUtil {

  public static void main(String[] args) throws IOException {
    // HttpclientDemo.upload("http://jwapi.dev.staff.xdf.cn:8080/import_excel?accessToken=5e83a0b0-d9a8-44c2-b2de-1158b8866ccc&appId=90101&businessType=2",
    // new File("C:/Users/ouzezhou/Desktop/Temp/20161223/班级模板 (7).xlsx"));

    System.out.println(HttpclientUtil.doGet("http://www.baidu.com",
                                            Collections.singletonList(new BasicHeader("testheader", "text/plain")),
                                            Collections.singletonList(new BasicClientCookie("testcookie", "xx"))));

//    HttpclientUtil.download("http://10.15.4.164:9080/jenkins/job/cdcadmin/ws/target/cdcadmin.war", new File("C:/Users/ouzezhou/Desktop/cdcadmin.war"));
  }

  public static String doGet(String url) {
    return doGet(url, null, null);
  }

  public static String doGet(String url, List<Header> headers, List<BasicClientCookie> cookies) {
    try (CloseableHttpClient httpclient = createHttpClient(url, cookies)) {
      HttpGet httpRequest = new HttpGet(url);

      return doRequest(httpclient, httpRequest, headers);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String doPost(String url, List<NameValuePair> params) {
    return doPost(url, params, null, null);
  }

  public static String doPost(String url, List<NameValuePair> params, List<Header> headers, List<BasicClientCookie> cookies) {
    try (CloseableHttpClient httpclient = createHttpClient(url, cookies)) {
      HttpPost httpRequest = new HttpPost(url);
      httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

      return doRequest(httpclient, httpRequest, headers);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String doPost(String url, String body) {
    return doPost(url, body, null, null);
  }

  public static String doPost(String url, String body, List<Header> headers, List<BasicClientCookie> cookies) {
    try (CloseableHttpClient httpclient = createHttpClient(url, cookies)) {
      HttpPost httpRequest = new HttpPost(url);
      if(StringUtils.isNotEmpty(body)) {
        httpRequest.setEntity(new StringEntity(body, "UTF-8"));
      }

      return doRequest(httpclient, httpRequest, headers);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static RequestConfig defaultRequestConfig() {
    RequestConfig.Builder builder = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(60000);
//    builder.setProxy(new HttpHost("127.0.0.1", 8888));
    return builder.build();
  }

  private static CloseableHttpClient createHttpClient(String url, List<BasicClientCookie> cookies) {
    HttpClientBuilder builder = HttpClients.custom();
    builder.setDefaultRequestConfig(defaultRequestConfig());

    if (cookies != null && !cookies.isEmpty()) {
      String domain = url.replaceFirst("^http://([^/:]+).*", "$1");
      CookieStore cookieStore = new BasicCookieStore();
      for (BasicClientCookie cookie : cookies) {
        // set domain
        if (cookie.getDomain() == null) {
          cookie.setDomain(domain);
        }
        // add cookie
        cookieStore.addCookie(cookie);
      }
      builder.setDefaultCookieStore(cookieStore);
    }

    return builder.build();
  }

  private static String doRequest(CloseableHttpClient httpclient, HttpRequestBase httpRequest, List<Header> headers) {
    if (headers != null && !headers.isEmpty()) {
      for (Header header : headers) {
        httpRequest.addHeader(header);
      }
    }

    try (CloseableHttpResponse response = httpclient.execute(httpRequest);) {
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        return parseResponse(response);
      } else {
        String mess = String.valueOf(response.getStatusLine().getStatusCode());
        Exception e1 = null;
        try {
          mess += "\n" + parseResponse(response);
        } catch (Exception e) {
          e1 = e;
        }
        RuntimeException e2 = new RuntimeException(mess);
        if (e1 != null) {
          e2.addSuppressed(e1);
        }
        throw e2;
      }
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      httpRequest.abort();
    }
  }

  private static String parseResponse(CloseableHttpResponse response) throws IOException {
    HttpEntity entity = response.getEntity();
    try {
      String responseStr = EntityUtils.toString(entity);
      return responseStr;
    } finally {
      EntityUtils.consumeQuietly(entity);
    }
  }

  /**
   * 上传文件
   */
  public static void upload(String url, File file) {
    try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
      // 创建请求
      HttpPost httpRequest = new HttpPost(url);

      // 添加附件
      MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
      multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
      multipartEntityBuilder.addBinaryBody("file", file);
      // multipartEntityBuilder.addBinaryBody("file2", file2);
      httpRequest.setEntity(multipartEntityBuilder.build());

      // 发送
      try (CloseableHttpResponse response = httpclient.execute(httpRequest);) {
        // 解析响应值
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        } else {
          throw new RuntimeException(response.getStatusLine().getStatusCode() + ":" + response.getStatusLine().getReasonPhrase());
        }
      } finally {
        httpRequest.abort();
      }
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 下载文件
   */
  public static void download(String url, File file) {
    try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
      HttpGet httpRequest = new HttpGet(url);
      try (CloseableHttpResponse response = httpclient.execute(httpRequest);) {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
          HttpEntity entity = response.getEntity();
          try(InputStream input = entity.getContent(); OutputStream output = new FileOutputStream(file);) {
            IOUtils.copy(input, output);
          } finally {
            EntityUtils.consumeQuietly(entity);
          }
        } else {
          throw new RuntimeException(response.getStatusLine().getStatusCode() + ":" + response.getStatusLine().getReasonPhrase());
        }
      } finally {
        httpRequest.abort();
      } 
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
}

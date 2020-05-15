package com.ozz.demo.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author ozz
 */
public class NetworkInterfaceDemo {
  protected Logger log = LoggerFactory.getLogger(getClass());

  public String getHostFromUrl(String url) {
    Pattern hostPattern = Pattern.compile("^http://([^:/]+)[:/]");
    Matcher m = hostPattern.matcher(url);
    if (m.find()) {
      return m.group(1);
    } else {
      throw new RuntimeException("Can not find host from url:'" + url + "'.");
    }
  }

  public String getIPByHost(String host) throws UnknownHostException {
    InetAddress address = InetAddress.getByName(host);
    return address.getHostAddress();
  }

  public String getUrlReplaceHostToIp(String url) throws UnknownHostException {
    String host = getHostFromUrl(url);
    String ip = getIPByHost(host);
    if (host.equals(ip)) {
      return url;
    } else {
      return url.replace(host, ip);
    }
  }

  public InetAddress getLocalHost() throws SocketException {
    Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
    while (nis.hasMoreElements()) {
      NetworkInterface ni = nis.nextElement();
      Enumeration<InetAddress> ias = ni.getInetAddresses();
      while (ias.hasMoreElements()) {
        InetAddress ia = ias.nextElement();
        if (ia instanceof Inet4Address && !ia.getHostAddress().equals("127.0.0.1")) {
          // System.out.println("ipv4: "+ia.getHostAddress());
          return ia;
        }
        // else if (ia instanceof Inet6Address && !ia.equals("")) {
        // System.out.println("ipv6: "+ ia.getHostAddress());
        // return ia;
        // }
      }
    }
    return null;
  }

  /**
   * please run in tomcat
   */
  public void getServerHttpPort() {
    try {
      MBeanServer server = null;
      if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
        server = MBeanServerFactory.findMBeanServer(null).get(0);
      } else {
        return;
      }

      Set<ObjectName> objectNames = server.queryNames(new ObjectName("*:type=Connector,*"),
                                                      Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
      Iterator<ObjectName> iterator = objectNames.iterator();
      ObjectName objectName = null;
      while (iterator.hasNext()) {
        objectName = (ObjectName) iterator.next();

        String protocol = server.getAttribute(objectName, "protocol").toString();
        String scheme = server.getAttribute(objectName, "scheme").toString();
        String port = server.getAttribute(objectName, "port").toString();
        log.info(protocol + " : " + scheme + " : " + port);
      }
    } catch (Exception e) {
      log.error(null, e);
    }
  }

  public String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      // 多次反向代理后会有多个ip值，第一个ip才是真实ip
      int index = ip.indexOf(",");
      if (index != -1) {
        return ip.substring(0, index);
      } else {
        return ip;
      }
    }
    ip = request.getHeader("X-Real-IP");
    if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      return ip;
    }
    return request.getRemoteAddr();
  }
}

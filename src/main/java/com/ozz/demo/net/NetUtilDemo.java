package com.ozz.demo.net;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetUtilDemo {
    public static void main(String[] args) {
        for (InetAddress item : getLocalHost()) {
            StaticLog.info(item.getHostAddress());
        }
    }

    /**
     *  检测本地端口可用性
     */
    public static boolean isValidPort(int port) {
        return NetUtil.isValidPort(port);
    }

    @SneakyThrows
    public static List<InetAddress> getLocalHost() {
        List<InetAddress> list = new ArrayList<>();
        List<InetAddress> list2 = new ArrayList<>();
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        //遍历每个网卡
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            Enumeration<InetAddress> ias = ni.getInetAddresses();
            //获取网卡下所有ip
            while (ias.hasMoreElements()) {
                InetAddress ia = ias.nextElement();
                if(ia.isLoopbackAddress()) {
                    continue;
                } else {
                    list.add(ia);
                }
            }
        }
        return list;
    }

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StrUtil.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}

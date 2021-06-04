package com.aly8246.common.util;


import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPUtil {
    public static IPUtil instance;

    public static IPUtil getInstance() {
        if (null == instance) {
            instance = new IPUtil();
        }
        return instance;
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     *
     * @return ip
     */
    public String getIpAddr(HttpServletRequest request) {
        // 获取X-Forwarded-For
        String ip = request.getHeader("x-forwarded-for");
        String realIp = request.getHeader("x-real-ip");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // 获取Proxy-Client-IP
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // WL-Proxy-Client-IP
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // 获取的IP实际上是代理服务器的地址，并不是客户端的IP地址
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                // 根据网卡取本机配置的IP
                try {
                    ip = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException ignored) {
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) { // "***.***.***.***".length()
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }

        /**
         * 如果代理地址不存在，则查询x-real-ip是否存在
         * 可能存在于代理转发和内网穿透的环境
         */
        if (!StringUtils.isEmpty(realIp)) {
            ip = realIp;
        }
        return ip;

    }

    /**
     * 获取本地ip
     *
     * @return 本地IP
     */
    public static String localIp() {
        String localIp = null;
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
        }
        return localIp;
    }

    /**
     * 是否是合法ipv4地址
     *
     * @param ip ipv4地址
     */
    public static boolean isIpv4Addr(String ip) {
        if (ip.length() < 7 || ip.length() > 15) {
            return false;
        }
        String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(ip);
        return mat.find();
    }

    /**
     * 去掉host信息
     *
     * @param url http://192.168.3.130:9003/user/callback/
     * @return /user/callback/
     */
    public static String getUri(String url) {
        // http://192.168.3.130:9003/user/callback/
        if (url.startsWith("http://") || url.startsWith("https://")) {
            url = url.replace("http://", "");
            url = url.replace("https://", "");

            // 192.168.3.130:9003/user/callback/
            String host = url.split("/")[0];

            // /user/callback/
            return url.replace(host, "");
        }

        return url;
    }

}

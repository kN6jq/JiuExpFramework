package org.jiu.utils;

import java.net.*;
import java.util.Objects;

public class ProxyManager extends Authenticator {
    public static boolean isenable;
    public static String proxyType;
    private static String proxyHost;
    private static int proxyPort;
    private static String proxyUser;
    private static String proxyPass;

    public static void setGlobalProxy(String type, String host, int port, String user, String pass) {
        proxyHost = host;
        proxyPort = port;
        proxyUser = user;
        proxyPass = pass;
        if (type.equals("SOCKS5")) {
            socksProxy();
        } else {
            httpProxy();
        }
    }
    public static Proxy Proxy(){
        if (isenable){
            if (proxyType.equals("SOCKS5")) {
                return socksProxy();
            } else {
                return httpProxy();
            }
        }
        return Proxy.NO_PROXY;
    }

    public static Proxy httpProxy() {
        // 设置HTTP代理认证
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                if (getRequestorType() == RequestorType.PROXY) {
                    return new PasswordAuthentication(proxyUser, proxyPass.toCharArray());
                }
                return null;
            }
        });

        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Objects.requireNonNull(proxyHost), proxyPort));
    }

    public static Proxy socksProxy() {
        // 设置SOCKS5代理认证
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                if (getRequestorType() == RequestorType.PROXY) {
                    return new PasswordAuthentication(proxyUser, proxyPass.toCharArray());
                }
                return null;
            }
        });

        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(Objects.requireNonNull(proxyHost), proxyPort));
    }
}

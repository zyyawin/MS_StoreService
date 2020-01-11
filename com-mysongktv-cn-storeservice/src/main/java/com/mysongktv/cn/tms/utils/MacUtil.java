package com.mysongktv.cn.tms.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author ZhangYang
 * @date 2020/1/2 18:08
 */
public class MacUtil {
    public static String getMACAddr() {
        try {
            NetworkInterface inetAddress = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] macAddr = inetAddress.getHardwareAddress();
            StringBuffer sb = new StringBuffer();
            String macstr = null;
            for (byte b : macAddr
            ) {
                sb.append(toHexString(b).toUpperCase());
                macstr = sb.toString();
                // System.out.println(toHexString(b).toUpperCase());
            }
            return macstr;
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toHexString(int integer) {
        String str = Integer.toHexString((int) (integer & 0xff));
        if (str.length() == 1) {
            str = "0" + str;
        }
        return str;

    }

    public static boolean maxMac(String mac1, String mac2) {
        if (mac1==null || mac2==null){
            return false;
        }
        if (mac1.equals(mac2)){
            return true;
        }
        //方法 二
        Long clientMac = Long.parseLong(mac1, 16);
        Long serverMac = Long.parseLong(mac2, 16);

        if (clientMac > serverMac) {
            return true;
        }
        if (clientMac < serverMac) {
            return false;
        }
        return false;
    }

    public static void main(String[] args) {
        String macAddr = getMACAddr();

        System.out.println(macAddr);
//        String mac1="3C970EBEDD5D";
//        String mac2="4C970EBEDD5D";
//        String s = maxMac(mac1, mac2);
//        System.out.println(s);
        //post 请求 [主:MaxMac地址;从:MinMac;date:now]
        //  [主:MaxMac地址;从:MinMac;date:now]
    }
}

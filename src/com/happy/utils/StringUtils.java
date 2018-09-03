package com.happy.utils;


import com.sun.jna.StringArray;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sylar on 2014/7/21.
 */
public class StringUtils {

    private static final String TOKEN = "7Ud9xCc280O";

    /**
     * 将字符串首字母变成小写
     *
     * @param s
     * @return
     */
    public static String lowerFirst(String s) {
        if (s == null || s.length() < 1) {
            return s;
        }
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    /**
     * 将字符串首字母变成大写
     *
     * @param s
     * @return
     */
    public static String upperFirst(String s) {
        if (s == null || s.length() < 1) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 判断字符串首字母是否小写
     *
     * @param s
     * @return
     */
    public static Boolean startsWithLower(String s) {
        if (s == null || s.length() < 1) {
            return false;
        }
        char c = s.charAt(0);
        return c >= 'a' && c <= 'z';
    }

    /**
     * 判断字符串首字母是否大写
     *
     * @param s
     * @return
     */
    public static Boolean startsWithUpper(String s) {
        if (s == null || s.length() < 1) {
            return false;
        }
        char c = s.charAt(0);
        return c >= 'A' && c <= 'Z';
    }

    /**
     * 将字符串进行MD5加密
     *
     * @param password
     * @return
     */
    public static String makeMD5(String password) {
        if (password == null) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(bt));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    /**
     * 将字符串进行SHA1加密
     *
     * @param inStr
     * @return
     */
    public static String makeSHA1(String inStr) {
        if (inStr == null) {
            return null;
        }
        try {
            MessageDigest mdsha1 = MessageDigest.getInstance("SHA-1");

            byte[] digest = mdsha1.digest(inStr.getBytes("utf-8"));
            StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < digest.length; i++) {
                String shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String left(String content, int len) {
        if (content == null) {
            return null;
        }
        if (content.length() > len) {
            return content.substring(0, len);
        }
        return content;
    }

    public static String encodeToBase64(String s) {
        if (s == null)
            return null;
        try {
            return Base64.encodeBase64String(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decodeFromBase64(String s) {
        return Base64.decodeBase64(s);
    }

    public static String fromBase64(String s) {
        try {
            return new String(org.apache.commons.codec.binary.Base64.decodeBase64(s), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isBase64(String s) {
        return Base64.isBase64(s);
    }

    public static String encodeToBase64(byte[] data) {
        return Base64.encodeBase64String(data);
    }

    public static String join(String[] args, String separator) {
        if (args == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(args.length);
        int i = 0;
        for (String s : args) {
            sb.append(s);
            i++;
            if (i < args.length) {
                sb.append(separator);
            }
        }

        return sb.toString();
    }

    public static String join(List args, String separator) {
        if (args == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(args.size());
        int i = 0;
        for (Object s : args) {
            sb.append(s);
            i++;
            if (i < args.size()) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String getFileExt(String fileName) {
        if (isEmpty(fileName) || fileName.indexOf(".") == -1 || fileName.endsWith(".")) {
            return "";
        } else {
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (ext.indexOf('?') > -1) {
                ext = ext.split("[?]")[0];
            }
            return ext;
        }
    }

    public static String encodeURI(String s) {
        try {
            return java.net.URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    public static boolean isNotEmpty(String s) {
        return s != null && !"".equals(s);
    }

    public static boolean isNumber(String s) {
        if (isNotEmpty(s)) {
            return s.matches("[0-9]+");
        }
        return false;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static String getPassword(String password) {
        return makeMD5(password + TOKEN);
    }

    public static double getDistanceFromXtoY(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk = (180 / 3.14169);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    public static String trim(String s) {
        if (isEmpty(s)) {
            return s;
        }
        return s.trim();
    }

    public static boolean isInStringArray(String s, String... stringArray) {
        if (s == null || stringArray == null) {
            return false;
        }
        for (String str : stringArray) {
            if (s.equals(str))
                return true;
        }
        return false;
    }
}

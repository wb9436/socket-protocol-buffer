package com.bomu.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isEmpty(String s) {
        return (s == null || s.isEmpty());
    }

    public static boolean isTrimEmpty(String s) {
        return (s == null || "".equals(s.trim()));
    }

    public static String getTrimStr(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * 手机号格式校验
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        Pattern p = Pattern.compile("^[1][2,3,4,5,6,7,8,9][0-9]{9}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * 校验是不是用户ID
     *
     * @param value
     * @return
     */
    public static boolean isUserId(String value) {
        Pattern p = Pattern.compile("^[1-9][0-9]{5,9}$");
        Matcher m = p.matcher(value);
        return m.matches();
    }

    public static int toInt(String s) {
        if (s == null || "".equals(s.trim())) {
            return 0;
        }
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
        }
        return 0;
    }

    public static String[] split(String dst, String regex) {
        if (dst == null || "".equals(dst))
            return new String[0];
        return dst.split(regex);
    }

    public static int[] toInts(String dst, String regex) {
        if (dst == null || "".equals(dst))
            return null;
        String[] a = dst.split(regex);
        int[] digital = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            digital[i] = Integer.parseInt(a[i]);
        }
        return digital;
    }

    /**
     * 校验是否合法的用户名
     *
     * @param username
     * @return
     */
    public static boolean validateUsername(String username) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9_]{3,10}");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    /**
     * 判断字符串是否合法有效(由汉字和字母组成,并且不超过长度(字节数)<code>len</code>)
     *
     * @param str
     * @param minLen 限制长度 字节数( 一个汉字2个字节 1个字符1个字节)
     * @return 0=字符串合法,-1=有其他字符,-2=字符串超过长度,-3=字符串太短, -4=字符串为空
     * @author LuZhiYong
     * @Date 2012-11-7
     */
    public static int isLegStr(String str, int minLen, int maxLen) {
        if (str == null) str = "";
        Pattern pattern1 = Pattern.compile("[a-zA-Z0-9 ]");
        // pattern2 = Pattern.compile("[\u4e00-\u9fa5]");//匹配中文字符
        Pattern pattern2 = Pattern.compile("[^\\x00-\\xff]");// 匹配双字节字符
        char[] strs = str.toCharArray();

        int count = 0;
        for (char s : strs) {
            Matcher matcher1 = pattern1.matcher(String.valueOf(s));
            Matcher matcher2 = pattern2.matcher(String.valueOf(s));
            if (matcher1.find()) {
                count++;
            } else if (matcher2.find()) {
                count = count + 2;
            } else {
                return -1;
            }
        }
        if (count > maxLen)
            return -2;
        if (count < minLen)
            return -3;
        return 0;
    }

    /**
     * @param s
     * @return
     * @author TanGuiyuan
     * @date 2012-11-15
     */
    public static byte[] getUtf8Bytes(String s) {
        try {
            if (s == null) {
                return null;
            }
            return s.getBytes("UTF-8");
        } catch (Exception e) {
            return s.getBytes();
        }
    }

    public static String byte2Utf8(byte[] content) {
        try {
            return new String(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(content);
        }
    }

    /**
     * @param s
     * @return
     * @author TanGuiyuan
     * @date 2012-11-15
     */
    public static byte[] getGBKBytes(String s) {
        try {
            return s.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            return s.getBytes();
        }
    }

    /**
     * @param s
     * @return
     * @author TanGuiyuan
     * @date 2012-11-15
     */
    public static byte[] getBytes(String s) {
        return s.getBytes();
    }

    /**
     * @param b
     * @return
     * @author TanGuiyuan
     * @date 2012-12-5
     */
    public static String toUTFString(byte[] b) {
        try {
            return new String(b, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(b);
        }
    }

    /**
     * 验证邮箱格式是否正确
     *
     * @param email
     * @return
     * @author TanGuiyuan
     * @date 2012-11-17
     */
    public static boolean isEmail(String email) {
        String regex = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /***
     * 替换字符串某个位置的字符
     *
     * @param idx
     *            位置
     * @param str0
     *            原字符串
     * @param str1
     *            替换的字符
     * @return Demo: idx=3,str0="0123456789",str1="x" return "012x456789";
     * @author LuZhiYong
     * @Date 2013-3-19
     */
    public static String repalce(int idx, String str0, String str1) {
        return str0.substring(0, idx) + str1 + str0.substring(idx + 1);
    }

    /****
     * 统计字符串中某个字符的数量
     *
     * @param srcStr
     * @param find
     * @return Demo srcStr="0001010", find='0' return 5;
     * @author LuZhiYong
     * @Date 2013-3-19
     */
    public static int countStr(String srcStr, char find) {
        if (srcStr == null || srcStr.isEmpty()) {
            throw new RuntimeException("srcStr is null.");
        }
        int num = 0;
        char[] chars = srcStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (find == chars[i])
                num++;
        }
        return num;
    }

    public static String binary2Hex(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] hex2Binary(String hexString) {
        if (StringUtils.isEmpty(hexString))
            throw new IllegalArgumentException("this hexString must not be empty");

        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    /**
     * @param s
     * @return
     * @author TanGuiyuan
     * @date 2012-11-15
     */
    public static byte[] getUTFBytes(String s) {
        try {
            if (s == null) {
                return null;
            }
            return s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return s.getBytes();
        }
    }

    /**
     * 创建订单ID
     *
     * @return
     */
    public static String createOrderId() {
        return DateUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + RandStrUtils.randNumeric(8);
    }

    /**
     * 金额格式化: 分转成元
     *
     * @param money 分
     * @return
     */
    public static String formatMoney(int money) {
        if (money > 0) {
            double d = (double) money / 100;
            DecimalFormat df = new DecimalFormat("#0.00");
            return df.format(d);
        }
        return "0";
    }

    /**
     * 获取请求参数字符串
     *
     * @param params 参数
     * @param encode 是否encode
     * @return
     */
    public static String getParameterString(Map<String, String> params, boolean encode) {
        Object[] keys = params.keySet().toArray();
        Arrays.sort(keys);
        StringBuffer buf = new StringBuffer();
        for (int index = 0; index < keys.length; index++) {
            String k = (String) keys[index];
            String v = params.get(k);
            if (!"sign".equals(k) && !"sign_type".equals(k) && v != null && !"".equals(v)) {
                if (encode) {
                    try {
                        v = URLEncoder.encode(v, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                    }
                }
                buf.append(k).append("=").append(v).append("&");
            }
        }
        buf.deleteCharAt(buf.length() - 1);
        return buf.toString();
    }

}

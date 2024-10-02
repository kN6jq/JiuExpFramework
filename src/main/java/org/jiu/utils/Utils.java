package org.jiu.utils;


import java.io.File;
import java.util.*;

public class Utils {

    // 生成指定位数的随机字符串
    public static String generateRandomString(int length) {
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
//
//    // 判断指定字符串是否以指定字符串结尾,如果是的话,删除掉指定字符串
//    public static String removeSuffix(String str, String suffix) {
//        if (str.endsWith(suffix)) {
//            str = str.substring(0, str.length() - 1);
//        }
//        return str;
//    }
//
//    // 对指定字符串进行base64编码,返回byte数组
//    public static String base64Encode(byte[] str) {
//        byte[] res = Base64.getEncoder().encode(str);
//        String res2 = new String(res);
//        res2 = res2.replace(System.lineSeparator(), "");
//        return res2;
//    }
//
//    public static String base64Encode(String str) {
//        byte[] b = new byte[]{};
//        b = str.getBytes(StandardCharsets.UTF_8);
//        return base64Encode(b);
//    }
//
//    public static byte[] base64Decode(String str) {
//        byte[] byteRes = new byte[]{};
//        byteRes = Base64.getDecoder().decode(str);
//        return byteRes;
//    }
//
//    // 对指定字符串进行base64解码
//    public static String base64Decode(byte[] bytes) {
//        byte[] res = Base64.getDecoder().decode(bytes);
//        return new String(res, StandardCharsets.UTF_8);
//    }
//
//    /**
//     * hex 编码
//     *
//     * @param src
//     * @return
//     */
//    public static String hex(byte[] src) {
//        StringBuilder sb = new StringBuilder();
//        for (byte b : src) {
//            String strHex = Integer.toHexString(b & 255);
//            sb.append(strHex.length() == 1 ? "0" + strHex : strHex);
//        }
//        return sb.toString().trim();
//    }
//
//    /**
//     * hex 解码
//     *
//     * @param src
//     * @return
//     */
//    public static byte[] unhex(String src) {
//        int byteLen = src.length() / 2;
//        byte[] ret = new byte[byteLen];
//        for (int i = 0; i < byteLen; i++) {
//            int m = (i * 2) + 1;
//            int n = m + 1;
//            int intVal = Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n)).intValue();
//            ret[i] = Byte.valueOf((byte) intVal).byteValue();
//        }
//        return ret;
//    }
//
//    /**
//     * 字符串转Unicode
//     *
//     * @param string
//     * @return
//     */
//    public static String string2Unicode(String string) {
//        StringBuffer unicode = new StringBuffer();
//        for (int i = 0; i < string.length(); i++) {
//            char c = string.charAt(i);
//            String tmp = Integer.toHexString(c);
//            if (tmp.length() >= 4) {
//                unicode.append("u" + Integer.toHexString(c));
//            } else if (tmp.length() == 3) {
//                unicode.append("u0" + Integer.toHexString(c));
//            } else if (tmp.length() == 2) {
//                unicode.append("u00" + Integer.toHexString(c));
//            } else if (tmp.length() == 1) {
//                unicode.append("u000" + Integer.toHexString(c));
//            } else if (tmp.length() == 3) {
//                unicode.append("u0000");
//            }
//        }
//        return unicode.toString();
//    }
//
//
//    // 对指定字符串进行md5加密
//    public static String md5(String str) {
//        return null;
//    }
//
//    /**
//     * gzip压缩
//     *
//     * @param bytes
//     * @return
//     * @throws IOException
//     */
//    public static String gzipCompress(byte[] bytes) throws IOException {
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        GZIPOutputStream gos = new GZIPOutputStream(os);
//        gos.write(bytes);
//        gos.close();
//        byte[] compressed = os.toByteArray();
//        os.close();
//        return new BASE64Encoder().encode(compressed);
//    }
//
//    /**
//     * gzip解压缩
//     *
//     * @param strEncode
//     * @return
//     * @throws IOException
//     */
//    public static byte[] gzipDecompress(String strEncode) throws IOException {
//        byte[] compressed = new BASE64Decoder().decodeBuffer(strEncode);
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressed);
//        GZIPInputStream gis = new GZIPInputStream(inputStream);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] data = new byte[Constants.ACC_ABSTRACT];
//        while (true) {
//            int bytesRead = gis.read(data);
//            if (bytesRead != -1) {
//                baos.write(data, 0, bytesRead);
//            } else {
//                return baos.toByteArray();
//            }
//        }
//    }
//
//
//    // 对指定字符串进行gzip解压缩
//    public static String ungzip(byte[] bytes) {
//        return null;
//    }
//
//    // 对指定字符串进行urlencode编码
//    public static String urlencode(String str) {
//        return null;
//    }
//
//    // 对指定字符串进行urldecode解码
//    public static String urldecode(String str) {
//        String result = "";
//        if (null == str) {
//            return "";
//        }
//        try {
//            result = java.net.URLDecoder.decode(str, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            return "";
//        }
//        return result;
//    }
//
//    // 返回当前时间戳
//    public static long getTimestamp() {
//        return 0;
//    }
//
//    // 返回时间戳,指定格式
//    public static String getTimestamp(String format) {
//        return null;
//    }
//
//    // 判断指定字符串是否为url格式
//    public static boolean isUrl(String url) {
//        if (url == null) {
//            return false;
//        }
//        String regEx = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
//                + "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
//                + "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
//                + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
//                + "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
//                + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
//                + "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
//                + "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";
//        Pattern p = Pattern.compile(regEx);
//        Matcher matcher = p.matcher(url);
//        return matcher.matches();
//    }
//
//    // 判断指定字符串是否为域名
//    public static boolean isDomain(String str) {
//        return false;
//    }
//
//    // 判断指定字符串是否为IP地址
//    public static boolean isIp(String str) {
//        return false;
//    }
//
//    // 将ip转为网段格式
//    public static String ipToNet(String ip) {
//        return null;
//    }
//
//    // 将网段格式转为ip
//    public static String netToIp(String net) {
//        return null;
//    }
//
//    // 去重IP地址
//    public static String removeDuplicateIp(String str) {
//        return null;
//    }
//
//    // 过滤内网IP地址
//    public static List<String> filterInternalIps(List<String> ipsList) {
//        List<String> publicIps = new ArrayList<>();
//        for (String ip : ipsList) {
//            String[] ipParts = ip.split("\\.");
//            int firstOctet = Integer.parseInt(ipParts[0]);
//            if (firstOctet != 10 &&
//                    (firstOctet != 172 || Integer.parseInt(ipParts[1]) < 16 || Integer.parseInt(ipParts[1]) > 31) &&
//                    (firstOctet != 192 || Integer.parseInt(ipParts[1]) != 168)) {
//                publicIps.add(ip);
//            }
//        }
//        return publicIps;
//    }
//
//    /**
//     * 创建压缩文件
//     *
//     * @param shellName    shell的文件名
//     * @param shellContent shell的文件内容
//     * @param zipFileName  压缩文件的名字，要加上绝对路径/tmp/1.zip
//     */
//    public static void writeShellZip(String shellName, String shellContent, String zipFileName) {
//
//        try {
//            //创建压缩包
//            ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFileName)));
//            //创建压缩包里的文件
//            zipOutputStream.putNextEntry(new ZipEntry(shellName));
//            byte[] bytes1 = shellContent.getBytes(StandardCharsets.UTF_8);
//            zipOutputStream.write(bytes1, 0, bytes1.length);    //将数据写入到压缩包里的文件里面
//            zipOutputStream.closeEntry();
//            zipOutputStream.flush();
//            zipOutputStream.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 转义正则特殊字符 $()*+.[]?\^{},|
//     * Reference:https://www.cnblogs.com/lovehansong/p/7874337.html
//     *
//     * @param keyword
//     * @return
//     */
//    public static String escapeExprSpecialWord(String keyword) {
//        if (keyword != null && !keyword.trim().isEmpty()) {
//            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|", ":"};
//            for (String key : fbsArr) {
//                if (keyword.contains(key)) {
//                    keyword = keyword.replace(key, "\\" + key);
//                }
//            }
//            keyword = keyword.replace("\r", "\\r");
//            keyword = keyword.replace("\n", "\\n");
//        }
//        return keyword;
//    }
//
//    /**
//     * 转移json特殊字符 $()*+.[]?{}/^-|"
//     * Renference: https://www.cnblogs.com/javalanger/p/10913838.html
//     *
//     * @param keyword
//     * @return
//     */
//    public static String escapeJsonString(String keyword) {
//        if (keyword != null && !keyword.trim().isEmpty()) {
//            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|", ":", "-"};
//            for (String key : fbsArr) {
//                if (keyword.contains(key)) {
//                    keyword = keyword.replace(key, "\\" + key);
//                }
//            }
//            keyword = keyword.replace("\r", "\\r");
//            keyword = keyword.replace("\n", "\\n");
//        }
//        return keyword;
//    }
//
//    /**
//     * 统计字符串str中有多少个字符串keyword
//     *
//     * @param str
//     * @param keyword
//     * @return 返回存在的个数
//     */
//    public static int getStringCount(String str, String keyword) {
//        int len = keyword.length();
//        int count = 0;
//        int pos = str.indexOf(keyword);
//        if (pos != -1) {
//            count += 1;
//            count += getStringCount(str.substring(pos + len), keyword);
//        } else {
//            return count;
//        }
//
//        return count;
//    }
//
//    /**
//     * 将字符串开头空格去掉
//     *
//     * @param str
//     * @return
//     */
//    public static String trimStart(String str) {
//        if (Objects.equals(str, "") || str == null) {
//            return str;
//        }
//
//        final char[] value = str.toCharArray();
//        int start = 0, last = str.length();
//        while ((start <= last) && (value[start] <= ' ')) {
//            start++;
//        }
//        if (start == 0) {
//            return str;
//        }
//        if (start >= last) {
//            return "";
//        }
//        return str.substring(start, last);
//    }
//
//    public static void main(String[] args) {
//        String s = string2Unicode("asdasd");
//        System.out.println(s);
//    }\\

    // 处理获取到的header 按换行分割
    public static HashMap<String, String> getHeaders(String headers) {
        HashMap<String, String> headersMap = new HashMap<>();

        if (headers == null || headers.isEmpty()) {
            return headersMap;
        }

        String[] lines = headers.split("\n");
        for (String line : lines) {
            String[] keyValue = line.split(":", 2); // 最多分割为两部分，避免值中包含冒号的问题
            if (keyValue.length == 2) {
                String key = keyValue[0].trim(); // 去除首尾空格
                String value = keyValue[1].trim(); // 去除首尾空格
                headersMap.put(key, value);
            }
        }

        return headersMap;
    }




    // 删除url中的最后一个/
    public static String removeTrailingSlash(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        if (str.endsWith("/")) {
            return str.substring(0, str.length() - 1); // 删除最后一个字符
        }

        return str; // 不以 '/' 结尾，返回原字符串
    }


    public static ArrayList<String> getJar() {
        ArrayList<String> jars = new ArrayList<>();
        File libDir = new File("lib");
        if (libDir.exists() && libDir.isDirectory()) {
            for (File file : Objects.requireNonNull(libDir.listFiles())) {
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    jars.add(file.getName());
                }
            }
        } else {
            System.err.println("Directory 'lib' does not exist.");
        }
        return jars;
    }

}

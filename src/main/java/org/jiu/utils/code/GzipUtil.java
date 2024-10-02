package org.jiu.utils.code;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {

    // gzip压缩字符串
    public static byte[] compressString(String input) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = null;

        try {
            gzipOutputStream = new GZIPOutputStream(outputStream);
            gzipOutputStream.write(input.getBytes("UTF-8")); // 假设输入字符串是UTF-8编码
            gzipOutputStream.close(); // 关闭流，完成压缩
        } catch (IOException e) {
            throw new RuntimeException("Error compressing string", e);
        } finally {
            if (gzipOutputStream != null) {
                try {
                    gzipOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("Error closing gzip output stream", e);
                }
            }
        }

        return outputStream.toByteArray();
    }

    // gzip解压字符串
    public static String decompress(byte[] compressedData) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
        GZIPInputStream gzipInputStream = null;
        StringBuilder result = new StringBuilder();

        try (InputStream inputStream = byteArrayInputStream) {
            gzipInputStream = new GZIPInputStream(inputStream);
            int data;
            while ((data = gzipInputStream.read()) != -1) {
                result.append((char) data);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error decompressing data", e);
        } finally {
            if (gzipInputStream != null) {
                try {
                    gzipInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("Error closing gzip input stream", e);
                }
            }
        }

        return result.toString();
    }
}

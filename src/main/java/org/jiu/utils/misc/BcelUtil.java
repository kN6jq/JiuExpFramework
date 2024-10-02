package org.jiu.utils.misc;


import com.sun.org.apache.bcel.internal.classfile.Utility;
import org.jiu.utils.file.FileUtil;

import java.io.IOException;


public class BcelUtil {

    public static byte[] decode(String strBCEL) throws IOException {
        return Utility.decode(strBCEL.replace("$$BCEL$$", ""), true);
    }

//    public static String encode(String classFilePath) throws Exception {
//        byte[] clazzBytes = FileUtil.getFileBytes(classFilePath);
//        return encode(clazzBytes);
//    }

    public static String encode(byte[] clazzBytes) throws IOException {
        return "$$BCEL$$" + Utility.encode(clazzBytes, true);
    }
}

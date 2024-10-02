package org.jiu;

import org.jiu.form.JiuExpForm;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import static org.jiu.utils.Utils.getJar;

public class JiuExpFramework {

    public static void main(String[] args) {
        // 创建 JiuExpForm 实例
        JiuExpForm jiuExpForm = new JiuExpForm();
        jiuExpForm.Start();
    }

}

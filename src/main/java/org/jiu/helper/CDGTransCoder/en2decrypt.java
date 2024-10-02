package org.jiu.helper.CDGTransCoder;

import org.jiu.form.CryptographyForm;
import org.jiu.templates.TabContent;

import javax.swing.*;

public class en2decrypt  extends JPanel implements TabContent {
    private JTabbedPane tabbedPane;
    private CryptographyForm cryptographyForm;

    // 修改下面参数
    public String name = "亿赛通加解密"; // 漏洞名
    public String product = "Cryptography";             // 类型
    public String desc = "针对亿赛通进行加解密";  // 描述
    public String author = "xm17";              // 作者
    public String className = "org.jiu.helper.CDGTransCoder.en2decrypt"; // 类名(必须)
    public String tabName = "亿赛通加解密";  // tab显示名称

    public en2decrypt() {
    }

    public en2decrypt(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
    @Override
    public void init() {
        cryptographyForm = new CryptographyForm();
    }

    @Override
    public JPanel getPanel() {
        return cryptographyForm.CryptographyForm;
    }

    @Override
    public String getTabName() {
        return tabName;
    }

    @Override
    public void exploit() {
//        cryptographyForm.encryptButton.addActionListener(new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String inputAreaText = cryptographyForm.inputArea.getText();
//                if (!inputAreaText.isEmpty()){
//                    try {
//                        cryptographyForm.outputArea.setText(CDGUtil.encode(inputAreaText));
//                    } catch (Exception ex) {
//                        throw new RuntimeException(ex);
//                    }
//
//                }
//            }
//        });
//        cryptographyForm.decryptButton.addActionListener(new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String inputAreaText = cryptographyForm.inputArea.getText();
//                if (!inputAreaText.isEmpty()){
//                    try {
//                        cryptographyForm.outputArea.setText(CDGUtil.decode(inputAreaText));
//                    } catch (Exception ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }
//            }
//        });
    }

    @Override
    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
}

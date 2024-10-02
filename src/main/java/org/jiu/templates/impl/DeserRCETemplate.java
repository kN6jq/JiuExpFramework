package org.jiu.templates.impl;

import org.jiu.templates.TabContent;
import org.jiu.form.DeserForm;

import javax.swing.*;

public class DeserRCETemplate implements TabContent {
    private JTabbedPane tabbedPane;



    public DeserRCETemplate() {
    }

    public DeserRCETemplate(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    @Override
    public void init() {

    }

    @Override
    public JPanel getPanel() {
        return new DeserForm().DeserForm;
    }

    @Override
    public String getTabName() {
        return "tabName";
    }

    @Override
    public void exploit() {

    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
}

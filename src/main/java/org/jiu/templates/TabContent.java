package org.jiu.templates;

import javax.swing.*;

public interface TabContent {

    void init();

    JPanel getPanel();

    String getTabName();

    void exploit();

    void setTabbedPane(JTabbedPane tabbedPane);
}

package org.jiu.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.icons.FlatSearchIcon;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jiu.templates.HelperTemplateTableModel;
import org.jiu.templates.TabContent;
import org.jiu.templates.VulTemplateTableModel;
import org.jiu.utils.ProxyManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JiuExpForm {
    private JFrame jFrame;
    public JPanel JiuPanel;
    private JTabbedPane pluginTabPanel;
    private JPanel vulSearchPanel;
    private JTextField vulSearchPanelkeyWordtextField; // vul的搜索条
    private JTable helperPluginTable;
    private JTextField helperSearchPanelkeyWordtextField; // helper的搜索条
    private JPanel helperSearchPanel;
    private JScrollPane vulPluginTablePanel;
    private JScrollPane helperPluginTablePanel;
    private JPanel pluginJpanel;
    private JPanel ExplotJpanel;
    private JPanel HelperJpanel;
    private JTabbedPane ExploitTabbedPanel; // exp的TabbedPanel
    private JTabbedPane HelperTabbedPanel; // helper的TabbedPanel
    private static JMenuBar menuBar;
    private JPanel vulPluginPanel;
    private static TableRowSorter<VulTemplateTableModel> sortervul;
    private static TableRowSorter<HelperTemplateTableModel> sorterhelper;
    private static JTextField addressField;
    private static JTextField portField;
    private static JTextField userField;
    private static JTextField passwordField;
    private static JRadioButton enableProxyRadioButton;
    private static JRadioButton disableProxyRadioButton;
    // 用于存储代理设置的Map
    private static Map<String, String> proxySettings = new HashMap<>();

    public JiuExpForm() {
        ExploitTabbedPanel.removeTabAt(0);
        HelperTabbedPanel.removeTabAt(0);

        // 加载vul相关
        List<Object[]> vultemplatesData = new ArrayList<>();
        try {
            vultemplatesData = loadVulTemplatesFromPackage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addVulTable(vultemplatesData);

        // 加载helper相关
        List<Object[]> helpertemplatesData = new ArrayList<>();
        try {
            helpertemplatesData = loadHelperTemplatesFromPackage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addHelperTable(helpertemplatesData);


        menuBar = new JMenuBar();
        JMenu menu = new JMenu("proxy");
        JMenuItem menuItem = new JMenuItem("Set Proxy");
        menu.add(menuItem);
        menuBar.add(menu);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showProxyDialog(jFrame);
            }
        });
    }

    private void addHelperTable(List<Object[]> templatesData) {
        HelperTemplateTableModel model = new HelperTemplateTableModel(templatesData);
        JTable table = new JTable(model);
        sorterhelper = new TableRowSorter<>(model);
        table.setRowSorter(sorterhelper);
        helperPluginTablePanel.setViewportView(table);
        // 设置第一列的长度
        table.getColumnModel().getColumn(0).setMinWidth(350);
        table.getColumnModel().getColumn(0).setMaxWidth(350);
        table.getColumnModel().getColumn(0).setPreferredWidth(350);

        // 设置第二列的长度
        table.getColumnModel().getColumn(1).setMinWidth(200);
        table.getColumnModel().getColumn(1).setMaxWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);

        // 设置第四列的长度
        table.getColumnModel().getColumn(3).setMinWidth(100);
        table.getColumnModel().getColumn(3).setMaxWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        // 隐藏第五列（索引为 4）
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);
        table.getColumnModel().getColumn(4).setPreferredWidth(0);

        // 设置表格第一列的内容居中显示
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // 设置鼠标放在第三列显示隐藏信息
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setHorizontalTextPosition(JLabel.CENTER);
                label.setIconTextGap(0);
                label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));
                label.setToolTipText((String) value); // 设置鼠标悬停时显示的提示文本
                return label;
            }
        };

        table.getColumnModel().getColumn(2).setCellRenderer(renderer);


        // 设置搜索
        helperSearchPanelkeyWordtextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search, Enter");
        helperSearchPanelkeyWordtextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, new FlatSearchIcon());
        helperSearchPanelkeyWordtextField.registerKeyboardAction(e -> {
                    String searchKeyWord = helperSearchPanelkeyWordtextField.getText().trim();
                    // 忽略大小写
                    sorterhelper.setRowFilter(RowFilter.regexFilter("(?i)" + searchKeyWord));
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
                JComponent.WHEN_FOCUSED
        );


        // 添加右键菜单
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem sendTo = new JMenuItem("go to");
        sendTo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的行
                int selectedRow = table.getSelectedRow();
                // 获取第4列的值
                String selectedValue = (String) table.getValueAt(selectedRow, 4);
                if (selectedRow != -1) {
                    addNewTab(selectedValue, HelperTabbedPanel);
                }
            }
        });
        popupMenu.add(sendTo);

        // 为表格添加鼠标监听器
        table.setComponentPopupMenu(popupMenu); // 在JTable中直接设置右键菜单
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    table.setRowSelectionInterval(row, row); // 选中右键点击的行
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void addVulTable(List<Object[]> templatesData) {
        VulTemplateTableModel model = new VulTemplateTableModel(templatesData);
        JTable table = new JTable(model);
        sortervul = new TableRowSorter<>(model);
        table.setRowSorter(sortervul);

        vulPluginTablePanel.setViewportView(table);
        // 设置第一列的长度
        table.getColumnModel().getColumn(0).setMinWidth(350);
        table.getColumnModel().getColumn(0).setMaxWidth(350);
        table.getColumnModel().getColumn(0).setPreferredWidth(350);

        // 设置第二列的长度
        table.getColumnModel().getColumn(1).setMinWidth(200);
        table.getColumnModel().getColumn(1).setMaxWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);

        // 设置第四列的长度
        table.getColumnModel().getColumn(3).setMinWidth(100);
        table.getColumnModel().getColumn(3).setMaxWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        // 隐藏第五列（索引为 4）
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);
        table.getColumnModel().getColumn(4).setPreferredWidth(0);

        // 设置表格第一列的内容居中显示
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // 设置鼠标放在第三列显示隐藏信息
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setHorizontalTextPosition(JLabel.CENTER);
                label.setIconTextGap(0);
                label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));
                label.setToolTipText((String) value); // 设置鼠标悬停时显示的提示文本
                return label;
            }
        };

        table.getColumnModel().getColumn(2).setCellRenderer(renderer);


        // 设置搜索
        vulSearchPanelkeyWordtextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search, Enter");
        vulSearchPanelkeyWordtextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, new FlatSearchIcon());
        vulSearchPanelkeyWordtextField.registerKeyboardAction(e -> {
                    String searchKeyWord = vulSearchPanelkeyWordtextField.getText().trim();
                    // 忽略大小写
                    sortervul.setRowFilter(RowFilter.regexFilter("(?i)" + searchKeyWord));
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
                JComponent.WHEN_FOCUSED
        );


        // 添加右键菜单
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem sendTo = new JMenuItem("go to");
        sendTo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的行
                int selectedRow = table.getSelectedRow();
                // 获取第4列的值
                String selectedValue = (String) table.getValueAt(selectedRow, 4);
                if (selectedRow != -1) {
                    addNewTab(selectedValue, ExploitTabbedPanel);
                }
            }
        });
        popupMenu.add(sendTo);

        // 为表格添加鼠标监听器
        table.setComponentPopupMenu(popupMenu); // 在JTable中直接设置右键菜单
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    table.setRowSelectionInterval(row, row); // 选中右键点击的行
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private static void showProxyDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Proxy Settings", true);
        dialog.setSize(400, 400);

        JLabel addressLabel = new JLabel("Proxy Address:");
        addressField = new JTextField();
        addressField.setText(proxySettings.getOrDefault("address", ""));

        JLabel portLabel = new JLabel("Port:");
        portField = new JTextField();
        portField.setText(proxySettings.getOrDefault("port", ""));

        JLabel userLabel = new JLabel("Username:");
        userField = new JTextField();
        userField.setText(proxySettings.getOrDefault("username", ""));

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        passwordField.setText(proxySettings.getOrDefault("password", ""));

        JLabel typeLabel = new JLabel("Proxy Type:");
        String[] proxyTypes = new String[]{"SOCKS5", "HTTP"};
        JComboBox<String> typeComboBox = new JComboBox<>(proxyTypes);
        typeComboBox.setSelectedItem(proxySettings.getOrDefault("proxyType", "SOCKS5"));

        enableProxyRadioButton = new JRadioButton("Enable Proxy");
        disableProxyRadioButton = new JRadioButton("Disable Proxy");

        ButtonGroup proxyGroup = new ButtonGroup();
        proxyGroup.add(enableProxyRadioButton);
        proxyGroup.add(disableProxyRadioButton);

        boolean enableProxy = Boolean.parseBoolean(proxySettings.getOrDefault("enableProxy", "false"));
        enableProxyRadioButton.setSelected(enableProxy);
        disableProxyRadioButton.setSelected(!enableProxy);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle OK button action
                String address = addressField.getText();
                String port = portField.getText();
                String username = userField.getText();
                String password = new String(passwordField.getText());
                String proxyType = (String) typeComboBox.getSelectedItem();
                boolean enableProxy = enableProxyRadioButton.isSelected();

                // 保存输入的代理设置
                proxySettings.put("address", address);
                proxySettings.put("port", port);
                proxySettings.put("username", username);
                proxySettings.put("password", password);
                proxySettings.put("proxyType", proxyType);
                proxySettings.put("enableProxy", String.valueOf(enableProxy));

                if (enableProxyRadioButton.isSelected()) {
                    assert proxyType != null;
                    ProxyManager.setGlobalProxy(proxyType, address, Integer.parseInt(port), username, password);
                    ProxyManager.isenable = true;
                    ProxyManager.proxyType = proxyType;
                } else if (disableProxyRadioButton.isSelected()) {
                    ProxyManager.isenable = false;
                }

                dialog.dispose();
            }
        });

        GroupLayout layout = new GroupLayout(dialog.getContentPane());
        dialog.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(addressLabel)
                                .addComponent(portLabel)
                                .addComponent(userLabel)
                                .addComponent(passwordLabel)
                                .addComponent(typeLabel)
                                .addComponent(enableProxyRadioButton)
                                .addComponent(disableProxyRadioButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(addressField)
                                .addComponent(portField)
                                .addComponent(userField)
                                .addComponent(passwordField)
                                .addComponent(typeComboBox)
                                .addComponent(okButton))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addressLabel)
                                .addComponent(addressField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(portLabel)
                                .addComponent(portField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(userLabel)
                                .addComponent(userField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordLabel)
                                .addComponent(passwordField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(typeLabel)
                                .addComponent(typeComboBox))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(enableProxyRadioButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(disableProxyRadioButton))
                        .addComponent(okButton)
        );

        dialog.setLocationRelativeTo(null); // Center the dialog on the screen
        dialog.setVisible(true); // Make the dialog visible
    }


    private static void addNewTab(String className, JTabbedPane tabbedPane) {
        Class<?> clazz = null;
        TabContent tabContent = null;
        try {
            clazz = Class.forName(className);
            tabContent = (TabContent) clazz.getDeclaredConstructor().newInstance(); // 创建实例
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        tabContent.init();
        tabContent.setTabbedPane(tabbedPane);

        // 创建一个新的标签页内容面板
        JPanel tabContentPanel = new JPanel();
        tabContentPanel.setLayout(new BorderLayout());
        tabContentPanel.add(tabContent.getPanel(), BorderLayout.CENTER);

        // 创建关闭按钮
        JButton closeButton = new JButton("x");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 通过遍历找到包含当前关闭按钮的标签页索引
                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    Component component = tabbedPane.getTabComponentAt(i);
                    if (component instanceof JPanel && ((JPanel) component).getComponentCount() > 1 && ((JPanel) component).getComponent(1) == closeButton) {
                        tabbedPane.removeTabAt(i);
                        break;
                    }
                }
            }
        });

        // 创建一个 JPanel 作为标签标题组件
        JPanel tabTitlePanel = new JPanel(new BorderLayout());
        JLabel tabTitleLabel = new JLabel(tabContent.getTabName());

        // 添加关闭按钮到标签标题面板的东侧
        tabTitlePanel.add(tabTitleLabel, BorderLayout.CENTER);
        tabTitlePanel.add(closeButton, BorderLayout.EAST);

        // 添加标签页到 OptionsTabbedPanel，并设置自定义的标签标题组件
        tabbedPane.addTab(null, tabContentPanel); // 标签标题设置为 null
        int index = tabbedPane.getTabCount() - 1; // 获取添加后的标签页索引
        tabbedPane.setTabComponentAt(index, tabTitlePanel); // 设置自定义的标签标题组件

        tabContent.exploit(); // 执行特定操作
    }

    public void Start() {
        SwingUtilities.invokeLater(() -> {
            // 加载主题
            initFlatLaf();
            // 创建窗口
            jFrame = new JFrame("JiuExpForm");
            jFrame.setJMenuBar(menuBar);
            jFrame.setLayout(new BorderLayout());
            jFrame.setSize(new Dimension(1200, 700));
            jFrame.setPreferredSize(new Dimension(1200, 700));
            jFrame.setLocationRelativeTo(null);
            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JiuExpForm jiuExpForm = new JiuExpForm();
            jFrame.setContentPane(jiuExpForm.JiuPanel);
            jFrame.setVisible(true);
        });
    }

    /**
     * 加载flatlaf主题
     */
    public static void initFlatLaf() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        UIManager.put("TextComponent.arc", 5);

    }


//    private static List<Object[]> loadVulTemplatesFromPackage() throws Exception {
//        List<Object[]> templatesData = new ArrayList<>();
//        ClassLoader classLoader = JiuExpForm.class.getClassLoader();
//        String path = "org.jiu.exploit".replace('.', '/');
//        URL resource = classLoader.getResource(path);
//
//        if (resource != null) {
//            File directory = new File(resource.toURI());
//            if (directory.exists() && directory.isDirectory()) {
//                loadClassesFromDirectory(directory, "org.jiu.exploit", templatesData);
//            }
//        }
//        return templatesData;
//    }
//
//    private static void loadClassesFromDirectory(File directory, String packageName, List<Object[]> templatesData) throws Exception {
//        File[] files = directory.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isDirectory()) {
//                    // 递归调用，处理子目录
//                    loadClassesFromDirectory(file, packageName + "." + file.getName(), templatesData);
//                } else if (file.getName().endsWith(".class")) {
//                    String className = packageName + '.' + file.getName().replace(".class", "");
//                    Class<?> cls = Class.forName(className);
//
//                    // 检查类是否实现了 TabContent 接口
//                    if (TabContent.class.isAssignableFrom(cls)) {
//                        // 使用反射获取公共字段
//                        Field[] fields = cls.getDeclaredFields();
//                        String name = null, product = null, desc = null, author = null, classNames = null;
//
//                        for (Field field : fields) {
//                            field.setAccessible(true); // 设置访问权限
//                            if (field.getName().equals("name")) {
//                                name = (String) field.get(cls.getDeclaredConstructor().newInstance());
//                            } else if (field.getName().equals("product")) {
//                                product = (String) field.get(cls.getDeclaredConstructor().newInstance());
//                            } else if (field.getName().equals("desc")) {
//                                desc = (String) field.get(cls.getDeclaredConstructor().newInstance());
//                            } else if (field.getName().equals("author")) {
//                                author = (String) field.get(cls.getDeclaredConstructor().newInstance());
//                            } else if (field.getName().equals("className")) {
//                                classNames = (String) field.get(cls.getDeclaredConstructor().newInstance());
//                            }
//                        }
//                        // 添加数据到列表
//                        templatesData.add(new Object[]{name, product, desc, author, classNames});
//                    }
//                }
//            }
//        }
//    }


    public static List<Object[]> loadHelperTemplatesFromPackage() throws Exception {
        List<Object[]> templatesData = new ArrayList<>();
        ClassLoader classLoader = JiuExpForm.class.getClassLoader();
        String path = "org/jiu/helper"; // 资源路径，斜杠分隔
        URL resource = classLoader.getResource(path);

        if (resource != null) {
            if (resource.getProtocol().equals("jar")) {
                // 处理 JAR 文件中的资源
                JarURLConnection jarConnection = (JarURLConnection) resource.openConnection();
                JarFile jarFile = jarConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                        String className = entryName.replace("/", ".").replace(".class", "");
                        Class<?> cls = Class.forName(className);

                        // 检查类是否实现了 TabContent 接口
                        if (TabContent.class.isAssignableFrom(cls)) {
                            // 使用反射获取公共字段
                            Field[] fields = cls.getDeclaredFields();
                            String name = null, product = null, desc = null, author = null, classNames = null;

                            for (Field field : fields) {
                                field.setAccessible(true); // 设置访问权限
                                if (field.getName().equals("name")) {
                                    name = (String) field.get(cls.getDeclaredConstructor().newInstance());
                                } else if (field.getName().equals("product")) {
                                    product = (String) field.get(cls.getDeclaredConstructor().newInstance());
                                } else if (field.getName().equals("desc")) {
                                    desc = (String) field.get(cls.getDeclaredConstructor().newInstance());
                                } else if (field.getName().equals("author")) {
                                    author = (String) field.get(cls.getDeclaredConstructor().newInstance());
                                } else if (field.getName().equals("className")) {
                                    classNames = (String) field.get(cls.getDeclaredConstructor().newInstance());
                                }
                            }
                            // 添加数据到列表
                            templatesData.add(new Object[]{name, product, desc, author, classNames});
                        }
                    }
                }
            } else {
                // 处理文件系统中的资源（如果资源在文件系统中）
                File directory = new File(resource.toURI());
                if (directory.exists() && directory.isDirectory()) {
                    loadClassesFromDirectory(directory, "org.jiu.helper", templatesData);
                }
            }
        }
        return templatesData;
    }

    public static List<Object[]> loadVulTemplatesFromPackage() throws Exception {
        List<Object[]> templatesData = new ArrayList<>();
        ClassLoader classLoader = JiuExpForm.class.getClassLoader();
        String path = "org/jiu/exploit"; // 资源路径，斜杠分隔
        URL resource = classLoader.getResource(path);

        if (resource != null) {
            if (resource.getProtocol().equals("jar")) {
                // 处理 JAR 文件中的资源
                JarURLConnection jarConnection = (JarURLConnection) resource.openConnection();
                JarFile jarFile = jarConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                        String className = entryName.replace("/", ".").replace(".class", "");
                        Class<?> cls = Class.forName(className);

                        // 检查类是否实现了 TabContent 接口
                        if (TabContent.class.isAssignableFrom(cls)) {
                            // 使用反射获取公共字段
                            Field[] fields = cls.getDeclaredFields();
                            String name = null, product = null, desc = null, author = null, classNames = null;

                            for (Field field : fields) {
                                field.setAccessible(true); // 设置访问权限
                                if (field.getName().equals("name")) {
                                    name = (String) field.get(cls.getDeclaredConstructor().newInstance());
                                } else if (field.getName().equals("product")) {
                                    product = (String) field.get(cls.getDeclaredConstructor().newInstance());
                                } else if (field.getName().equals("desc")) {
                                    desc = (String) field.get(cls.getDeclaredConstructor().newInstance());
                                } else if (field.getName().equals("author")) {
                                    author = (String) field.get(cls.getDeclaredConstructor().newInstance());
                                } else if (field.getName().equals("className")) {
                                    classNames = (String) field.get(cls.getDeclaredConstructor().newInstance());
                                }
                            }
                            // 添加数据到列表
                            templatesData.add(new Object[]{name, product, desc, author, classNames});
                        }
                    }
                }
            } else {
                // 处理文件系统中的资源（如果资源在文件系统中）
                File directory = new File(resource.toURI());
                if (directory.exists() && directory.isDirectory()) {
                    loadClassesFromDirectory(directory, "org.jiu.exploit", templatesData);
                }
            }
        }
        return templatesData;
    }

    private static void loadClassesFromDirectory(File directory, String packageName, List<Object[]> templatesData) throws Exception {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归调用，处理子目录
                    loadClassesFromDirectory(file, packageName + "." + file.getName(), templatesData);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().replace(".class", "");
                    Class<?> cls = Class.forName(className);

                    // 检查类是否实现了 TabContent 接口
                    if (TabContent.class.isAssignableFrom(cls)) {
                        // 使用反射获取公共字段
                        Field[] fields = cls.getDeclaredFields();
                        String name = null, product = null, desc = null, author = null, classNames = null;

                        for (Field field : fields) {
                            field.setAccessible(true); // 设置访问权限
                            if (field.getName().equals("name")) {
                                name = (String) field.get(cls.getDeclaredConstructor().newInstance());
                            } else if (field.getName().equals("product")) {
                                product = (String) field.get(cls.getDeclaredConstructor().newInstance());
                            } else if (field.getName().equals("desc")) {
                                desc = (String) field.get(cls.getDeclaredConstructor().newInstance());
                            } else if (field.getName().equals("author")) {
                                author = (String) field.get(cls.getDeclaredConstructor().newInstance());
                            } else if (field.getName().equals("className")) {
                                classNames = (String) field.get(cls.getDeclaredConstructor().newInstance());
                            }
                        }
                        // 添加数据到列表
                        templatesData.add(new Object[]{name, product, desc, author, classNames});
                    }
                }
            }
        }
    }






    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        JiuPanel = new JPanel();
        JiuPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        pluginTabPanel = new JTabbedPane();
        JiuPanel.add(pluginTabPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        pluginJpanel = new JPanel();
        pluginJpanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        pluginTabPanel.addTab("Plugin", pluginJpanel);
        final JTabbedPane tabbedPane1 = new JTabbedPane();
        pluginJpanel.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Vul Plugin", panel1);
        vulSearchPanel = new JPanel();
        vulSearchPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(vulSearchPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        vulSearchPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Keyword");
        vulSearchPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        vulSearchPanelkeyWordtextField = new JTextField();
        vulSearchPanel.add(vulSearchPanelkeyWordtextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        vulPluginTablePanel = new JScrollPane();
        vulPluginTablePanel.setEnabled(false);
        panel1.add(vulPluginTablePanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        vulPluginPanel = new JPanel();
        vulPluginPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        vulPluginTablePanel.setViewportView(vulPluginPanel);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Helper Plugin", panel2);
        helperSearchPanel = new JPanel();
        helperSearchPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(helperSearchPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        helperSearchPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label2 = new JLabel();
        label2.setText("Keyword");
        helperSearchPanel.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        helperSearchPanelkeyWordtextField = new JTextField();
        helperSearchPanel.add(helperSearchPanelkeyWordtextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        helperPluginTablePanel = new JScrollPane();
        panel2.add(helperPluginTablePanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        helperPluginTable = new JTable();
        helperPluginTablePanel.setViewportView(helperPluginTable);
        ExplotJpanel = new JPanel();
        ExplotJpanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        pluginTabPanel.addTab("Exploit", ExplotJpanel);
        ExploitTabbedPanel = new JTabbedPane();
        ExplotJpanel.add(ExploitTabbedPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        ExploitTabbedPanel.addTab("Untitled", panel3);
        HelperJpanel = new JPanel();
        HelperJpanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        pluginTabPanel.addTab("Helper", HelperJpanel);
        HelperTabbedPanel = new JTabbedPane();
        HelperJpanel.add(HelperTabbedPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        HelperTabbedPanel.addTab("Untitled", panel4);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return JiuPanel;
    }


}


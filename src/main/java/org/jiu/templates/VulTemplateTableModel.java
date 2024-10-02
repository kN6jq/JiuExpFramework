package org.jiu.templates;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class VulTemplateTableModel extends AbstractTableModel {
    private List<Object[]> templatesData;
    private String[] columnNames = {"Name", "Product", "Description", "Author","ClassName"};

    public VulTemplateTableModel(List<Object[]> templatesData) {
        this.templatesData = templatesData;
    }

    @Override
    public int getRowCount() {
        return templatesData.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return templatesData.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    // 添加删除行的方法
    public void removeRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < templatesData.size()) {
            templatesData.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex); // 通知表格数据发生变化
        }
    }
}
package de.sitl.dev.pov.viewer2.gui;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class CameraTable extends JTable {
    
    private final CameraTableModel tableModel;
    
    private Class<?> lastClass;
    
    public CameraTable(CameraTableModel tableModel) {
        super(tableModel);
        this.tableModel = tableModel;
    }
    
    @Override
    public Class<?> getColumnClass(int column) {
        if (this.lastClass == null) {
            return super.getColumnClass(column);
        } else {
            return this.lastClass;
        }
    }
    
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        this.lastClass = null;
        final Class<?> cellClass = this.tableModel.getCellClass(row, column);
        final Object value = this.tableModel.getValueAt(row, column);
        assert cellClass.isInstance(value);
        return getDefaultRenderer(cellClass);
    }
    
    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        this.lastClass = this.tableModel.getCellClass(row, column);
        return this.getDefaultEditor(this.lastClass);
    }

}

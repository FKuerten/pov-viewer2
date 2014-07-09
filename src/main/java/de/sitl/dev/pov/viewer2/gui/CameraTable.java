package de.sitl.dev.pov.viewer2.gui;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Because JTable is too limited for our purpose.
 * 
 * @author Fabian K&uuml;rten
 */
public class CameraTable extends JTable {
    
    /**
     * The data model.
     */
    private final CameraTableModel tableModel;
    
    /**
     * Used to exploit JTables behavior.
     */
    private Class<?> lastClass;
    
    /**
     * Creates a table with a given model.
     * 
     * @param tableModel
     *            the model
     */
    public CameraTable(CameraTableModel tableModel) {
        super(tableModel);
        this.tableModel = tableModel;
    }
    
    /*
     * Some special handling for our column classes.
     * Unfortunately JTable does not specify the row in this method.
     * We exploit JTable's behaviour (i.e, calling getCellEditor() before getColumnClass())
     */
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

package de.sitl.dev.pov.viewer2.gui;

import java.util.NoSuchElementException;

import javax.swing.table.AbstractTableModel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import de.sitl.dev.pov.viewer2.api.camera.CameraChangeListener;
import de.sitl.dev.pov.viewer2.api.camera.CameraChangedEvent;
import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadableCamera;
import de.sitl.dev.pov.viewer2.api.camera.WritableCamera;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadWritableRoundingCamera;

/**
 * A table data model for cameras.
 * 
 * @author Fabian K&uuml;rten
 */
class CameraTableModel extends AbstractTableModel {
    
    /**
     * The rows in our table.
     * 
     * @author Fabian K&uuml;rten
     */
    @RequiredArgsConstructor
    private static enum Rows {
        /**
         * The header row.
         */
        HEADER(""), X("x"), Y("y"), Z("z"), PHI("φ"), THETA("θ"), FOV("fov"),
            SPOTLIGHT("spotlight");
        
        @Getter
        private final String name;

        /**
         * Helper method to translate row index to Row.
         * 
         * @param index
         *            row index
         * @return enum element
         */
        static CameraTableModel.Rows valueOf(int index) {
            return Rows.values()[index];
        }
    }

    /**
     * Number of rows.
     */
    static final int ROW_COUNT = Rows.values().length;
    
    /***
     * Number of columns.
     */
    static final int COLUMN_COUNT = 3;

    /**
     * The normal camera.
     */
    private final ReadWritableCamera camera;
    /**
     * The rounded camera.
     */
    private final ReadWritableRoundingCamera roundingCamera;
    
    /**
     * A listener for camera changes, we need to update the view if the camera
     * changes.
     */
    private final CameraChangeListener listener;
    
    /**
     * Creates the table model
     * 
     * @param camera
     *            the camera
     * @param roundingCamera
     *            the rounded view
     */
    public CameraTableModel(ReadWritableCamera camera,
            ReadWritableRoundingCamera roundingCamera) {
        this.camera = camera;
        this.roundingCamera = roundingCamera;
        this.listener = new CameraChangeListener() {
            
            @Override
            public void cameraChanged(CameraChangedEvent event) {
                CameraTableModel.this.fireTableRowsUpdated(0, ROW_COUNT - 1);
            }
        };
        this.camera.addChangeListener(this.listener);
        this.roundingCamera.addChangeListener(this.listener);
    }

    @Override
    public int getRowCount() {
        return ROW_COUNT;
    }
    
    @Override
    public final int getColumnCount() {
        return COLUMN_COUNT;
    }
    
    @Override
    public final String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "";
            case 1:
                return "Camera";
            case 2:
                return "Rounded Camera";
            default:
                throw new NoSuchElementException();
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (rowIndex == 0 || columnIndex == 0) {
            // headers
            return false;
        } else {
            return true;
        }
    }
    
    @SuppressWarnings("boxing")
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // First select for correct camera
        ReadableCamera currentCamera;
        CameraTableModel.Rows row = Rows.valueOf(rowIndex);
        switch (columnIndex) {
            case 0:
                return row.getName();
            case 1:
                currentCamera = this.camera;
                break;
            case 2:
                currentCamera = this.roundingCamera;
                break;
            default:
                throw new IllegalArgumentException();
        }
        switch (row) {
            case HEADER:
                return this.getColumnName(columnIndex);
            case X:
                return currentCamera.getX();
            case Y:
                return currentCamera.getY();
            case Z:
                return currentCamera.getZ();
            case PHI:
                return currentCamera.getPhi();
            case THETA:
                return currentCamera.getTheta();
            case FOV:
                return currentCamera.getFOV();
            case SPOTLIGHT:
                return currentCamera.hasSpotlight();
            default:
                throw new IllegalArgumentException();
        }
    }
    
    @SuppressWarnings("boxing")
    @Override
    public void setValueAt(Object v, int rowIndex, int columnIndex) {
        // First select the correct camera
        WritableCamera currentCamera;
        CameraTableModel.Rows row = Rows.valueOf(rowIndex);
        switch (columnIndex) {
            case 0:
                throw new IllegalArgumentException();
            case 1:
                currentCamera = this.camera;
                break;
            case 2:
                currentCamera = this.roundingCamera;
                break;
            default:
                throw new IllegalArgumentException();
        }
        switch (row) {
            case HEADER:
                throw new IllegalArgumentException();
            case X:
                currentCamera.setX((Double)v);
                break;
            case Y:
                currentCamera.setY((Double)v);
                break;
            case Z:
                currentCamera.setZ((Double)v);
                break;
            case PHI:
                currentCamera.setPhi((Double)v);
                break;
            case THETA:
                currentCamera.setTheta((Double)v);
                break;
            case FOV:
                currentCamera.setFOV((Double)v);
                break;
            case SPOTLIGHT:
                currentCamera.setSpotlight((Boolean)v);
                break;
        }
    }
    
    /**
     * The default {@link #getColumnClass(int)} method ignores rows. However in
     * our table the class of a cell also depends on the row.
     * 
     * @param rowIndex
     *            the row index
     * @param columnIndex
     *            the column index
     * @return the class of the cell
     */
    public Class<?> getCellClass(int rowIndex, int columnIndex) {
        CameraTableModel.Rows row = Rows.valueOf(rowIndex);
        if (columnIndex == 0) {
            return String.class;
        } else {
            switch (row) {
                case HEADER:
                    return String.class;
                case X:
                case Y:
                case Z:
                    return Double.class;
                case PHI:
                case THETA:
                case FOV:
                    return Double.class;
                case SPOTLIGHT:
                    return Boolean.class;
                default:
                    throw new NoSuchElementException();
            }
        }
    }

}

package de.sitl.dev.pov.viewer2.gui;

import java.util.NoSuchElementException;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import de.sitl.dev.pov.viewer2.api.camera.CameraChangeListener;
import de.sitl.dev.pov.viewer2.api.camera.CameraChangedEvent;
import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadableCamera;
import de.sitl.dev.pov.viewer2.api.camera.WritableCamera;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadWritableRoundingCamera;

class CameraTableModel extends AbstractTableModel implements TableModel {
    
    @RequiredArgsConstructor
    private static enum Rows {
        HEADER(""), X("x"), Y("y"), Z("z"), PHI("φ"), THETA("θ"), FOV("fov"),
            SPOTLIGHT("flashlight");
        
        @Getter
        private final String name;

        static CameraTableModel.Rows valueOf(int index) {
            return Rows.values()[index];
        }
    }

    static final int ROW_COUNT = Rows.values().length;
    static final int COLUMN_COUNT = 3;

    private final ReadWritableCamera camera;
    private final ReadWritableRoundingCamera roundingCamera;
    private final CameraChangeListener listener;
    
    public CameraTableModel(ReadWritableCamera camera,
            ReadWritableRoundingCamera roundingCamera) {
        this.camera = camera;
        this.roundingCamera = roundingCamera;
        this.listener = new CameraChangeListener() {
            
            @Override
            public void stateChanged(CameraChangedEvent event) {
                CameraTableModel.this.fireTableRowsUpdated(0, ROW_COUNT - 1);
            }
        };
        this.camera.addChangeListener(this.listener);
        this.roundingCamera.addChangeListener(this.listener);
    }

    public int getRowCount() {
        return ROW_COUNT;
    }
    
    public final int getColumnCount() {
        return COLUMN_COUNT;
    }
    
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
        ReadableCamera camera;
        CameraTableModel.Rows row = Rows.valueOf(rowIndex);
        switch (columnIndex) {
            case 0:
                return row.getName();
            case 1:
                camera = this.camera;
                break;
            case 2:
                camera = this.roundingCamera;
                break;
            default:
                throw new IllegalArgumentException();
        }
        switch (row) {
            case HEADER:
                return this.getColumnName(columnIndex);
            case X:
                return camera.getX();
            case Y:
                return camera.getY();
            case Z:
                return camera.getZ();
            case PHI:
                return camera.getPhi();
            case THETA:
                return camera.getTheta();
            case FOV:
                return camera.getFOV();
            case SPOTLIGHT:
                return camera.hasSpotlight();
            default:
                throw new IllegalArgumentException();
        }
    }
    
    @Override
    public void setValueAt(Object v, int rowIndex, int columnIndex) {
        WritableCamera camera;
        CameraTableModel.Rows row = Rows.valueOf(rowIndex);
        switch (columnIndex) {
            case 0:
                throw new IllegalArgumentException();
            case 1:
                camera = this.camera;
                break;
            case 2:
                camera = this.roundingCamera;
                break;
            default:
                throw new IllegalArgumentException();
        }
        switch (row) {
            case HEADER:
                throw new IllegalArgumentException();
            case X:
                camera.setX((Double)v);
                break;
            case Y:
                camera.setY((Double)v);
                break;
            case Z:
                camera.setZ((Double)v);
                break;
            case PHI:
                camera.setPhi((Double)v);
                break;
            case THETA:
                camera.setTheta((Double)v);
                break;
            case FOV:
                camera.setFOV((Double)v);
                break;
            case SPOTLIGHT:
                camera.setSpotlight((Boolean)v);
                break;
        }
    }
    
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

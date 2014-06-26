package de.sitl.dev.pov.viewer2.gui;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadWritableRoundingCamera;
import de.sitl.dev.pov.viewer2.api.roundingcamera.WritableRoundingCamera;

public class CameraStatusView extends JPanel {
    
    private final JTable dataTable;

    public CameraStatusView(ReadWritableCamera camera,
            ReadWritableRoundingCamera roundingCamera) {
        
        CameraTableModel dataModel =
            new CameraTableModel(camera, roundingCamera);

        this.dataTable = new CameraTable(dataModel);
        this.add(this.dataTable);
    }
}

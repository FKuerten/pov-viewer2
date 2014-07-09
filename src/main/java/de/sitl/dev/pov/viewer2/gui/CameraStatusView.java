package de.sitl.dev.pov.viewer2.gui;

import javax.swing.JPanel;

import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadWritableRoundingCamera;

/**
 * Display the status of the camera and its rounded view. Also allows modifying
 * the camera.
 * 
 * @author Fabian K&uuml;rten
 */
public class CameraStatusView extends JPanel {
    
    /**
     * This table display the data.
     */
    private final CameraTable dataTable;

    /**
     * Creates the status view.
     * 
     * @param camera
     *            the camera
     * @param roundingCamera
     *            the rounded view
     */
    public CameraStatusView(ReadWritableCamera camera,
            ReadWritableRoundingCamera roundingCamera) {
        
        CameraTableModel dataModel =
            new CameraTableModel(camera, roundingCamera);

        this.dataTable = new CameraTable(dataModel);
        this.add(this.dataTable);
    }
}

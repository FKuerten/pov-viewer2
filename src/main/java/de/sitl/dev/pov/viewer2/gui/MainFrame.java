package de.sitl.dev.pov.viewer2.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.imageSource.ImageSource;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadWritableRoundingCamera;

/**
 * The main frame of the application.
 * 
 * @author Fabian K&uuml;rten
 */
public class MainFrame extends JFrame {
    
    /**
     * Used to display the scene.
     */
    final SceneView sceneView;
    
    /**
     * Display the status of the camera.
     */
    private final CameraStatusView cameraStatusView;
    
    /**
     * Creates the frame
     * 
     * @param camera
     *            the normal camera
     * @param roundingCamera
     *            the rounded view
     * @param imageSource
     *            a source for images
     */
    public MainFrame(ReadWritableCamera camera,
            ReadWritableRoundingCamera roundingCamera, ImageSource imageSource) {
        this.setLayout(new BorderLayout());
        this.sceneView = new SceneView(camera, roundingCamera, imageSource);
        this.cameraStatusView = new CameraStatusView(camera, roundingCamera);
        
        this.add(this.sceneView, BorderLayout.CENTER);
        this.add(this.cameraStatusView, BorderLayout.EAST);
        
        this.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosed(WindowEvent e) {
                MainFrame.this.sceneView.active = false;
            }
            
        });
    }
    
}

package de.sitl.dev.pov.viewer2.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.imageSource.ImageSource;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadWritableRoundingCamera;

public class MainFrame extends JFrame {
    
    private final ReadWritableCamera camera;
    private final SceneView sceneView;
    private final CameraStatusView cameraStatusView;
    
    public MainFrame(ReadWritableCamera camera,
            ReadWritableRoundingCamera roundingCamera, ImageSource imageSource) {
        this.camera = camera;
        
        this.setLayout(new BorderLayout());
        this.sceneView = new SceneView(camera, roundingCamera, imageSource);
        this.cameraStatusView = new CameraStatusView(camera, roundingCamera);
        
        this.add(this.sceneView, BorderLayout.CENTER);
        this.add(this.cameraStatusView, BorderLayout.EAST);
    }
    
}

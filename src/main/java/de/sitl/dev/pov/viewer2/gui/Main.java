package de.sitl.dev.pov.viewer2.gui;

import javax.swing.JFrame;

import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadWritableRoundingCamera;
import de.sitl.dev.pov.viewer2.impl.camera.CameraData;
import de.sitl.dev.pov.viewer2.impl.roundingcamera.RoundedCameraDecorator;

public class Main {
    
    public static void main(String[] args) {
        
        ReadWritableCamera camera = new CameraData();
        ReadWritableRoundingCamera roundingCamera =
            new RoundedCameraDecorator(camera);
        
        MainFrame mainFrame = new MainFrame(camera, roundingCamera);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setVisible(true);
    }
    
}

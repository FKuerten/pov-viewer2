package de.sitl.dev.pov.viewer2.gui;

import java.io.File;

import javax.swing.WindowConstants;

import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.imageSource.ImageSource;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadWritableRoundingCamera;
import de.sitl.dev.pov.viewer2.api.scene.ReadableScene;
import de.sitl.dev.pov.viewer2.impl.camera.CameraData;
import de.sitl.dev.pov.viewer2.impl.imageSource.PovRayImageSource;
import de.sitl.dev.pov.viewer2.impl.roundingcamera.RoundedCameraDecorator;
import de.sitl.dev.pov.viewer2.impl.scene.SceneData;

/**
 * The main class.
 * 
 * @author Fabian K&uuml;rten
 */
public class Main {
    
    /**
     * The main method
     * 
     * @param args
     *            ignored for now
     */
    public static void main(String[] args) {
        
        final File directory = new File("target/pov");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        final ReadableScene scene =
            new SceneData("/home/fabian/data/coding/povray/stone/stone.pov");
        
        ReadWritableCamera camera = new CameraData(scene);
        ReadWritableRoundingCamera roundingCamera =
            new RoundedCameraDecorator(camera);
        ImageSource imageSource = new PovRayImageSource(directory);
        
        MainFrame mainFrame =
            new MainFrame(camera, roundingCamera, imageSource);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setVisible(true);
    }
    
}

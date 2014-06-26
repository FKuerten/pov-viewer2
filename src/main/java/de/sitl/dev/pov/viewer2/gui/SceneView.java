package de.sitl.dev.pov.viewer2.gui;

import javax.swing.JPanel;

import lombok.RequiredArgsConstructor;
import de.sitl.dev.pov.viewer2.api.camera.Camera;

@RequiredArgsConstructor
public class SceneView extends JPanel {
    
    private final Camera camera;

}

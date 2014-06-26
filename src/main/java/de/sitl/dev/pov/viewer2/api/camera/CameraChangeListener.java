package de.sitl.dev.pov.viewer2.api.camera;

import javax.swing.event.ChangeListener;

public interface CameraChangeListener {
    
    void stateChanged(CameraChangedEvent event);
}

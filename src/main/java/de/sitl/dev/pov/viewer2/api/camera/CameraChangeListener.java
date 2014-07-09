package de.sitl.dev.pov.viewer2.api.camera;

/**
 * Used to listen for {@link CameraChangedEvent}s.
 * 
 * @author Fabian K&uuml;rten
 */
public interface CameraChangeListener {
    
    /**
     * The camera changed.
     * 
     * @param event
     */
    void cameraChanged(CameraChangedEvent event);
}

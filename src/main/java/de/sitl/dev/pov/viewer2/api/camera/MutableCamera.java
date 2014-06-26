package de.sitl.dev.pov.viewer2.api.camera;

import javax.swing.event.ChangeListener;

/**
 * As opposed to ImmutableCamera this interface guarantees that the camera
 * <strong>may</strong> change. This alone is of course rather useless as it
 * cannot guarantee that the camara will <strong>change</strong>. However
 * mutable objects can be observed.
 * 
 * @author Fabian K&uuml;rten
 */
public interface MutableCamera extends Camera {
    
    void addChangeListener(CameraChangeListener changeListener);
    
    void removeChangeListener(CameraChangeListener changeListener);

}

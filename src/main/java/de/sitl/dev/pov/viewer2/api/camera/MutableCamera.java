package de.sitl.dev.pov.viewer2.api.camera;


/**
 * As opposed to {@link ImmutableCamera} this interface guarantees that the
 * camera <strong>may</strong> change. This alone is of course rather useless as
 * it cannot guarantee that the camara <strong>will</strong> change. However
 * mutable objects can be observed.
 * 
 * @author Fabian K&uuml;rten
 */
public interface MutableCamera extends Camera {
    
    /**
     * Add a listener to this camera.
     * 
     * @param changeListener
     *            the listener
     */
    void addChangeListener(CameraChangeListener changeListener);
    
    /**
     * Remove a listener from this camera.
     * 
     * @param changeListener
     *            the listener
     */
    void removeChangeListener(CameraChangeListener changeListener);

}

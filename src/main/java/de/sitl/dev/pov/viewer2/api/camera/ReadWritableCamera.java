package de.sitl.dev.pov.viewer2.api.camera;

/**
 * A camera that can be both read and written. This allows us to
 * increment/decrement values.
 * 
 * @author Fabian K&uuml;rten
 */
public interface ReadWritableCamera extends ReadableCamera, WritableCamera {
    
    /**
     * Moves the camera forward along its current viewing direction.
     * 
     * @param forward
     *            distance to travel
     */
    void strafeForward(double forward);
    
    /**
     * Moves the camera to the left, according to its current viewing direction.
     * 
     * @param left
     *            distance to travel
     */
    void strafeLeft(double left);
    
    /**
     * Rotates the camera clock wise.
     * 
     * @param deltaPhi
     *            angle to travel, in degrees
     */
    void rotateCW(double deltaPhi);
    
}

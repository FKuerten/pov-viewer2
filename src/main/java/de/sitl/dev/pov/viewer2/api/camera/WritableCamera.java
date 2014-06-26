package de.sitl.dev.pov.viewer2.api.camera;

import de.sitl.dev.pov.viewer2.api.scene.Scene;

/**
 * A camera that can be modified.
 * 
 * @author Fabian K&uuml;rten
 */
public interface WritableCamera extends MutableCamera {
    
    /**
     * Sets the level of detail
     * 
     * @param levelOfDetail
     *            between 0 and 1
     */
    void setLevelOfDetail(double levelOfDetail);

    /**
     * Left/right rotation The angle will be normalized.
     * 
     * @param phi
     *            angle
     */
    void setPhi(double phi);
    
    /**
     * The scene to use.
     * 
     * @param scene
     *            a scene
     */
    void setScene(Scene scene);
    
    /**
     * 90 is straight up, -90 straight down
     * 
     * @param theta
     *            up/down angle
     */
    void setTheta(double theta);
    
    /**
     * Sets part of the location.
     * 
     * @param x
     */
    void setX(double x);
    
    /**
     * Sets part of the location.
     * 
     * @param y
     */
    void setY(double y);
    
    /**
     * Sets part of the location.
     * 
     * @param z
     */
    void setZ(double z);
    
    /**
     * Changes the field of view
     * 
     * @param fovAngle
     */
    void setFOV(double fovAngle);
    
    /**
     * Turns the flashlight on or off.
     * 
     * @param spotlight
     *            new state of the flashlight
     */
    void setSpotlight(boolean spotlight);
}

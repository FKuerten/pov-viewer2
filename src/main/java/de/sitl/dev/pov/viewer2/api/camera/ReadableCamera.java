package de.sitl.dev.pov.viewer2.api.camera;

import de.sitl.dev.pov.viewer2.api.scene.ReadableScene;
import de.sitl.dev.pov.viewer2.api.scene.Scene;

/**
 * Base interface for Cameras. Cameras have:
 * <ul>
 * <li>a location &lt;x,y,z&gt;</li>
 * <li>look into a specific direction, defined by theta (up/down) and
 * phi(left/right)</li>
 * <li>a field of view angle</li>
 * <li>level of detail</li>
 * <li>the world they are in</li>
 * <li>a flash light</li>
 * </ul>
 * 
 * @author Fabian K&uuml;rten
 */
public interface ReadableCamera extends Camera {
    
    /**
     * @return x position "breadth"
     */
    public abstract double getX();
    
    /**
     * @return y position "height"
     */
    public abstract double getY();
    
    /**
     * @return z position "depth"
     */
    public abstract double getZ();
    
    /**
     * 90 is straight up, -90 straight down
     * 
     * @return up/down angle
     */
    public abstract double getTheta();
    
    /**
     * @return left/right angle
     */
    public abstract double getPhi();
    
    /**
     * @return field of view angle
     */
    public abstract double getFOV();
    
    /**
     * between [0,1]
     * 
     * @return level of detail
     */
    public abstract double getLevelOfDetail();
    
    /**
     * @return scene
     */
    public abstract ReadableScene getScene();
    
    /**
     * A spotlight illuminates the objects in front of the camera.
     * 
     * @return spotlight enabled
     */
    public boolean hasSpotlight();
    
    ImmutableCamera getAsImmutableCamera();
    
    /**
     * @return an equality preserving string
     */
    String getAsString();
}

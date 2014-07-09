package de.sitl.dev.pov.viewer2.api.camera;


/**
 * Base interface for Cameras. Cameras have:
 * <ul>
 * <li>a location &lt;x,y,z&gt;</li>
 * <li>look into a specific direction, defined by theta (up/down) and
 * phi(left/right). Both are in degrees.</li>
 * <li>a field of view angle</li>
 * <li>level of detail</li>
 * <li>the world they are in</li>
 * <li>a flash light</li>
 * </ul>
 * 
 * @author Fabian K&uuml;rten
 */
public interface Camera {
    // intentionally empty
}

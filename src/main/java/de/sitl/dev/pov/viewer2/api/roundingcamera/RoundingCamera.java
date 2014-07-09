package de.sitl.dev.pov.viewer2.api.roundingcamera;

import de.sitl.dev.pov.viewer2.api.camera.Camera;

/**
 * A rounding camera is a camera that returns only rounded values. This is used
 * to generate a "snap-to-grid" behavior which allows us to cache images.
 * 
 * @author Fabian K&uuml;rten
 */
public interface RoundingCamera extends Camera {
    // intentionally empty
}

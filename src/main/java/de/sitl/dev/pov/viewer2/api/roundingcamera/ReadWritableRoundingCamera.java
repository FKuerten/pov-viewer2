package de.sitl.dev.pov.viewer2.api.roundingcamera;

import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;

/**
 * A rounding camera that can be both read and written.
 * 
 * @author Fabian K&uuml;rten
 */
public interface ReadWritableRoundingCamera extends ReadableRoundingCamera,
        WritableRoundingCamera, ReadWritableCamera {
    // intentionally empty
}

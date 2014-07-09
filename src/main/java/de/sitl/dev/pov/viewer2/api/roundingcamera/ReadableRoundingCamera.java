package de.sitl.dev.pov.viewer2.api.roundingcamera;

import de.sitl.dev.pov.viewer2.api.camera.ReadableCamera;

/**
 * A camera that returns rounded values. This interface introduces methods for
 * querying the step size
 * 
 * @author Fabian K&uuml;rten
 */
public interface ReadableRoundingCamera extends ReadableCamera, RoundingCamera {
    
    /**
     * @return the smallest change in coordinates
     */
    double getSmallestCoordinateStep();
    
    /**
     * @return the smallest change in left/right rotation
     */
    double getSmallestPhiStep();
    
    /**
     * @return the smallest change in up/down rotation
     */
    double getSmallestThetaStep();
    
    /**
     * @return the smallest change in field of view
     */
    double getSmallestFOVStep();
    
    /**
     * @return the smallest change in the level of detail
     */
    double getSmallestLODStep();
}

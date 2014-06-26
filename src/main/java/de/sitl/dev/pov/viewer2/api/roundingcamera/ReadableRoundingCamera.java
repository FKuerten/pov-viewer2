package de.sitl.dev.pov.viewer2.api.roundingcamera;

import de.sitl.dev.pov.viewer2.api.camera.ReadableCamera;

/**
 * A camera that returns rounded values.
 * 
 * @author Fabian K&uuml;rten
 */
public interface ReadableRoundingCamera extends ReadableCamera, RoundingCamera {
    
    double getSmallestCoordinateStep();
    
    double getSmallestPhiStep();
    
    double getSmallestThetaStep();
    
    double getSmallestFOVStep();
    
    double getSmallestLODStep();
}

package de.sitl.dev.pov.viewer2.api.roundingcamera;

import de.sitl.dev.pov.viewer2.api.camera.WritableCamera;

public interface WritableRoundingCamera extends RoundingCamera,
        WritableCamera {
    
    void setSmallestCoordinateStep(double step);
    
    void setSmallestPhiStep(double step);
    
    void setSmallestThetaStep(double step);
    
    void setSmallestFOVStep(double step);
    
    void setSmallestLODStep(double step);

}

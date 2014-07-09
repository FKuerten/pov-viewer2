package de.sitl.dev.pov.viewer2.api.roundingcamera;

import de.sitl.dev.pov.viewer2.api.camera.WritableCamera;

/**
 * Allows to set the smallest steps.
 * 
 * @author Fabian K&uuml;rten
 */
public interface WritableRoundingCamera extends RoundingCamera,
        WritableCamera {
    
    /**
     * Sets the smallest coordinate step
     * 
     * @param step
     *            new smallest step
     */
    void setSmallestCoordinateStep(double step);
    
    /**
     * Sets the smallest left/right angle step
     * 
     * @param step
     *            new smallest step, degrees
     */
    void setSmallestPhiStep(double step);

    
    /**
     * Sets the smallest up/down angle step
     * 
     * @param step
     *            new smallest step, degrees
     */
    void setSmallestThetaStep(double step);
    
    /**
     * Sets the smallest field of view angle step
     * 
     * @param step
     *            new smallest step, degrees
     */
    void setSmallestFOVStep(double step);
    
    /**
     * Sets the smallest level of detail step
     * 
     * @param step
     *            new smallest step
     */
    void setSmallestLODStep(double step);

}

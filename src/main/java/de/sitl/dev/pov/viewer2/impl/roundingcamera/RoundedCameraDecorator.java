package de.sitl.dev.pov.viewer2.impl.roundingcamera;

import lombok.Data;
import lombok.experimental.Delegate;
import de.sitl.dev.pov.viewer2.api.camera.WritableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadWritableRoundingCamera;
import de.sitl.dev.pov.viewer2.api.scene.Scene;

@Data
public class RoundedCameraDecorator implements ReadWritableRoundingCamera {
    
    @Delegate(types = { WritableCamera.class })
    private final ReadWritableCamera camera;
    
    private double smallestCoordinateStep = 1;
    private double smallestPhiStep = 15;
    private double smallestThetaStep = 15;
    private double smallestFOVStep = 5;
    private double smallestLODStep = 0.1;
    
    private double rC(double c) {
        return Math.round(c / this.smallestCoordinateStep)
            * this.smallestCoordinateStep;
    }
    
    private double rPhi(double c) {
        return Math.round(c / this.smallestPhiStep) * this.smallestPhiStep;
    }
    
    private double rTheta(double c) {
        return Math.round(c / this.smallestThetaStep) * this.smallestThetaStep;
    }
    
    private double rFov(double c) {
        return Math.round(c / this.smallestFOVStep) * this.smallestFOVStep;
    }
    
    private double rLOD(double c) {
        return Math.round(c / this.smallestLODStep) * this.smallestLODStep;
    }

    @Override
    public double getX() {
        return this.rC(this.camera.getX());
    }
    
    @Override
    public double getY() {
        return this.rC(this.camera.getY());
    }
    
    @Override
    public double getZ() {
        return this.rC(this.camera.getZ());
    }
    
    @Override
    public double getTheta() {
        return this.rTheta(this.camera.getTheta());
    }
    
    @Override
    public double getPhi() {
        return this.rPhi(this.camera.getPhi());
    }
    
    @Override
    public double getFOV() {
        return this.rFov(this.camera.getFOV());

    }
    
    @Override
    public double getLevelOfDetail() {
        return this.rLOD(this.camera.getLevelOfDetail());
    }
    
    @Override
    public Scene getScene() {
        return this.camera.getScene();
    }
    
    @Override
    public boolean hasSpotlight() {
        return this.camera.hasSpotlight();
    }

}

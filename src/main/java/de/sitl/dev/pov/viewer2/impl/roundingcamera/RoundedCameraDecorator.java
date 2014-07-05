package de.sitl.dev.pov.viewer2.impl.roundingcamera;

import lombok.Data;
import lombok.experimental.Delegate;
import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.camera.WritableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadWritableRoundingCamera;
import de.sitl.dev.pov.viewer2.api.scene.ReadableScene;
import de.sitl.dev.pov.viewer2.api.scene.Scene;
import de.sitl.dev.pov.viewer2.impl.camera.ImmutableCameraImplementation;

@Data
public class RoundedCameraDecorator implements ReadWritableRoundingCamera {
    
    @Delegate(types = { WritableCamera.class })
    private final ReadWritableCamera camera;
    
    private double smallestCoordinateStep = 1;
    private double smallestPhiStep = 360.0 / 16 / 2;
    private double smallestThetaStep = 180.0 / 8 / 2;
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
    public ReadableScene getScene() {
        return this.camera.getScene();
    }
    
    @Override
    public boolean hasSpotlight() {
        return this.camera.hasSpotlight();
    }

    @Override
    public ImmutableCamera getAsImmutableCamera() {
        return new ImmutableCameraImplementation(this);
    }
    
    @Override
    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(this.getX()).append(",").append(this.getY())
            .append(",").append(this.getZ()).append(">");
        sb.append("<").append(this.getPhi()).append(",")
            .append(this.getTheta()).append(">");
        sb.append("[").append(this.getFOV()).append("]");
        sb.append("[").append(this.hasSpotlight()).append("]");
        sb.append("(").append(this.getScene().getName()).append(")");
        sb.append("[").append(this.getLevelOfDetail()).append("]");
        return sb.toString();
    }

}

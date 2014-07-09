package de.sitl.dev.pov.viewer2.impl.roundingcamera;

import lombok.Data;
import lombok.experimental.Delegate;
import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.camera.WritableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadWritableRoundingCamera;
import de.sitl.dev.pov.viewer2.api.scene.ReadableScene;
import de.sitl.dev.pov.viewer2.impl.camera.ImmutableCameraImplementation;

/**
 * A decorator for a {@link ReadWritableCamera} that provides a rounded view of
 * its fields.
 * 
 * @author Fabian K&uuml;rten
 */
@Data
public class RoundedCameraDecorator implements ReadWritableRoundingCamera {
    
    /**
     * The camera we decorate.
     */
    @Delegate(types = { WritableCamera.class })
    private final ReadWritableCamera camera;
    
    /**
     * The smallest coordinate change.
     */
    private double smallestCoordinateStep = 1;
    
    /**
     * The smallest left/right change.
     */
    private double smallestPhiStep = 360.0 / 16 / 2;
    
    /**
     * The smallest up/down change.
     */
    private double smallestThetaStep = 180.0 / 8 / 2;
    
    /**
     * The smallest field of view change.
     */
    private double smallestFOVStep = 5;
    
    /**
     * The smallest level of detail step.
     */
    private double smallestLODStep = 0.1;
    
    /**
     * Rounds a coordinate.
     * 
     * @param c
     *            the coordinate
     * @return rounded value
     */
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
    
    /**
     * Helper function to the the sine of phi.
     * 
     * @return sin(phi)
     */
    double getSinPhi() {
        return Math.sin(this.getPhi() / 180 * Math.PI);
    }
    
    /**
     * Helper function to the the cosine of phi.
     * 
     * @return cos(phi)
     */
    double getCosPhi() {
        return Math.cos(this.getPhi() / 180 * Math.PI);
    }
    
    /**
     * Helper function to the the sine of theta.
     * 
     * @return sin(theta)
     */
    double getSinTheta() {
        return Math.sin(this.getTheta() / 180 * Math.PI);
    }
    
    /**
     * Helper function to the the cosine of theta.
     * 
     * @return cos(theta)
     */
    double getCosTheta() {
        return Math.cos(this.getTheta() / 180 * Math.PI);
    }
    
    @Override
    public void strafeForward(double forward) {
        final double horizontal = forward * this.getCosTheta();
        final double vertical = forward * this.getSinTheta();
        this.setZ(this.getZ() + horizontal * this.getCosPhi());
        this.setY(this.getY() + vertical);
        this.setX(this.getX() + horizontal * -this.getSinPhi());
    }
    
    @Override
    public void strafeLeft(double left) {
        this.setX(this.getX() + left * -this.getCosPhi());
        this.setZ(this.getZ() + left * -this.getSinPhi());
    }
    
    @Override
    public void rotateCW(double deltaPhi) {
        this.setPhi(this.getPhi() + deltaPhi);
    }

}

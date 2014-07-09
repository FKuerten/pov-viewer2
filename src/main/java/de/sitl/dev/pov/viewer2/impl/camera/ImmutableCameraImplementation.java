package de.sitl.dev.pov.viewer2.impl.camera;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadableCamera;
import de.sitl.dev.pov.viewer2.api.scene.ImmutableScene;

/**
 * An immutable camera.
 * 
 * @author Fabian K&uuml;rten
 */
@Data
public class ImmutableCameraImplementation implements ImmutableCamera {
    
    /**
     * Coordinates
     */
    final double x, y, z;
    
    /**
     * Rotation in degrees.
     */
    final double phi, theta;
    
    /**
     * Field of view in degrees.
     */
    final double fOV;
    
    /**
     * Level of detail.
     */
    final double levelOfDetail;
    
    /**
     * Whether we have a spotlight.
     */
    @Getter(AccessLevel.NONE)
    final private boolean spotlight;
    
    /**
     * The scene to display.
     */
    final private ImmutableScene scene;
    
    @Override
    public boolean hasSpotlight() {
        return this.spotlight;
    }
    
    /**
     * Copy constructor.
     * 
     * @param camera
     *            original camera.
     */
    public ImmutableCameraImplementation(ReadableCamera camera) {
        this.x = camera.getX();
        this.y = camera.getY();
        this.z = camera.getZ();
        this.phi = camera.getPhi();
        this.theta = camera.getTheta();
        this.fOV = camera.getFOV();
        this.levelOfDetail = camera.getLevelOfDetail();
        this.spotlight = camera.hasSpotlight();
        this.scene = camera.getScene().getAsImmutableScene();
    }

    @Override
    public ImmutableCamera getAsImmutableCamera() {
        return this;
    }
    
    @Override
    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(this.x).append(",").append(this.y).append(",")
            .append(this.z).append(">");
        sb.append("<").append(this.phi).append(",").append(this.theta)
            .append(">");
        sb.append("[").append(this.fOV).append("]");
        sb.append("[").append(this.spotlight).append("]");
        sb.append("(").append(this.scene.getName()).append(")");
        sb.append("[").append(this.levelOfDetail).append("]");
        return sb.toString();
    }

}

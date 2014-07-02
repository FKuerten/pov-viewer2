package de.sitl.dev.pov.viewer2.impl.camera;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadableCamera;
import de.sitl.dev.pov.viewer2.api.scene.ImmutableScene;
import de.sitl.dev.pov.viewer2.api.scene.Scene;

@Data
public class ImmutableCameraImplementation implements ImmutableCamera {
    
    final double x, y, z;
    final double phi, theta;
    final double fOV;
    final double levelOfDetail;
    
    @Getter(AccessLevel.NONE)
    final private boolean spotlight;
    
    final private ImmutableScene scene;
    
    @Override
    public boolean hasSpotlight() {
        return this.spotlight;
    }
    
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

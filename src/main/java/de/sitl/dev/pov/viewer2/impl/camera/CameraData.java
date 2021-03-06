package de.sitl.dev.pov.viewer2.impl.camera;

import java.util.LinkedHashSet;
import java.util.Set;

import de.sitl.dev.pov.viewer2.api.camera.Camera;
import de.sitl.dev.pov.viewer2.api.camera.CameraChangeListener;
import de.sitl.dev.pov.viewer2.api.camera.CameraChangedEvent;
import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadableCamera;
import de.sitl.dev.pov.viewer2.api.scene.ReadableScene;
import de.sitl.dev.pov.viewer2.api.scene.Scene;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Simple data class for {@link Camera}.
 * 
 * @author Fabian K&uuml;rten
 */
@Data
@RequiredArgsConstructor
public class CameraData implements ReadWritableCamera {
    
    /**
     * Coordinates
     */
    double x, y, z;
    
    /**
     * Rotation angles in degrees.
     */
    double phi, theta;
    
    /**
     * Field of view in degrees.
     */
    double fOV = 90;
    
    /**
     * Level of detail.
     */
    double levelOfDetail;
    
    /**
     * The spotlight.
     */
    @Getter(AccessLevel.NONE)
    private boolean spotlight;
    
    /**
     * The scene.
     */
    private final ReadableScene scene;
    
    @Override
    public boolean hasSpotlight() {
        return this.spotlight;
    }
    
    /**
     * Copy-like constructor.
     * 
     * @param camera
     *            camera to copy from
     */
    public CameraData(ReadableCamera camera) {
        this.x = camera.getX();
        this.y = camera.getY();
        this.z = camera.getZ();
        this.phi = camera.getPhi();
        this.theta = camera.getTheta();
        this.fOV = camera.getFOV();
        this.levelOfDetail = camera.getLevelOfDetail();
        this.spotlight = camera.hasSpotlight();
        this.scene = camera.getScene();
    }

    /**
     * The change listeners.
     */
    private Set<CameraChangeListener> listeners = new LinkedHashSet<>();
    
    /**
     * Helper function to the the sine of phi.
     * 
     * @return sin(phi)
     */
    double getSinPhi() {
        return Math.sin(this.phi / 180 * Math.PI);
    }
    
    /**
     * Helper function to the the cosine of phi.
     * 
     * @return cos(phi)
     */
    double getCosPhi() {
        return Math.cos(this.phi / 180 * Math.PI);
    }
    
    /**
     * Helper function to the the sine of theta.
     * 
     * @return sin(theta)
     */
    double getSinTheta() {
        return Math.sin(this.theta / 180 * Math.PI);
    }
    
    /**
     * Helper function to the the cosine of theta.
     * 
     * @return cos(theta)
     */
    double getCosTheta() {
        return Math.cos(this.theta / 180 * Math.PI);
    }

    @Override
    public final void setX(double x) {
        this.x = x;
        this.fireStateChanged();
    }
    
    @Override
    public final void setY(double y) {
        this.y = y;
        this.fireStateChanged();
    }
    
    @Override
    public final void setZ(double z) {
        this.z = z;
        this.fireStateChanged();
    }
    
    @Override
    public final void setPhi(double _phi) {
        @SuppressWarnings("hiding")
        double phi = _phi;
        while (phi > 360) {
            phi -= 360;
        }
        while (phi < 0) {
            phi += 360;
        }
        this.phi = phi;
        this.fireStateChanged();
    }
    
    @Override
    public final void setTheta(double theta) {
        if (theta > 90 || theta < -90) {
            throw new IllegalArgumentException();
        }
        this.theta = theta;
        this.fireStateChanged();
    }
    
    @Override
    public final void setFOV(double fOV) {
        if (fOV < 0 || fOV > 180) {
            throw new IllegalArgumentException();
        }
        this.fOV = fOV;
        this.fireStateChanged();
    }
    
    @Override
    public final void setLevelOfDetail(double levelOfDetail) {
        this.levelOfDetail = levelOfDetail;
        this.fireStateChanged();
    }
    
    @Override
    public final void setSpotlight(boolean spotlight) {
        this.spotlight = spotlight;
        this.fireStateChanged();
    }
    
    @Override
    public final void setScene(Scene scene) {
        throw new Error("Not implemented, needs thought!");
    }

    @Override
    public void addChangeListener(CameraChangeListener changeListener) {
        this.listeners.add(changeListener);
    }
    
    @Override
    public void removeChangeListener(CameraChangeListener changeListener) {
        this.listeners.remove(changeListener);
    }

    /**
     * Notifies all listeners that the camera was modified.
     */
    private void fireStateChanged() {
        CameraChangedEvent event = new CameraChangedEvent();
        for (CameraChangeListener listener : this.listeners) {
            listener.cameraChanged(event);
        }
    }

    @Override
    public ImmutableCamera getAsImmutableCamera() {
        return new ImmutableCameraImplementation(this);
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
    
    @Override
    public void strafeForward(double forward) {
        final double horizontal = forward * this.getCosTheta();
        final double vertical = forward * this.getSinTheta();
        this.z += horizontal * this.getCosPhi();
        this.y += vertical;
        this.x += horizontal * -this.getSinPhi();
        this.fireStateChanged();
    }
    
    @Override
    public void strafeLeft(double left) {
        this.x += left * -this.getCosPhi();
        this.z += left * -this.getSinPhi();
        this.fireStateChanged();
    }
    
    @Override
    public void rotateCW(double deltaPhi) {
        this.phi += deltaPhi;
        this.fireStateChanged();
    }

}

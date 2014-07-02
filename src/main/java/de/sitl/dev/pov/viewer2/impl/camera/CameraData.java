package de.sitl.dev.pov.viewer2.impl.camera;

import java.util.LinkedHashSet;
import java.util.Set;

import de.sitl.dev.pov.viewer2.api.camera.CameraChangeListener;
import de.sitl.dev.pov.viewer2.api.camera.CameraChangedEvent;
import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadableCamera;
import de.sitl.dev.pov.viewer2.api.scene.ImmutableScene;
import de.sitl.dev.pov.viewer2.api.scene.ReadableScene;
import de.sitl.dev.pov.viewer2.api.scene.Scene;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CameraData implements ReadWritableCamera {
    
    double x, y, z;
    double phi, theta;
    double fOV = 90;
    double levelOfDetail;
    
    @Getter(AccessLevel.NONE)
    private boolean spotlight;
    
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

    private Set<CameraChangeListener> listeners = new LinkedHashSet<>();
    
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
    public final void setPhi(double phi) {
        this.phi = phi;
        this.fireStateChanged();
    }
    
    @Override
    public final void setTheta(double theta) {
        this.theta = theta;
        this.fireStateChanged();
    }
    
    @Override
    public final void setFOV(double fOV) {
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
            listener.stateChanged(event);
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

}

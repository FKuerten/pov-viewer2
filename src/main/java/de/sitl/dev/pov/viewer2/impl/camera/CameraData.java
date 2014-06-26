package de.sitl.dev.pov.viewer2.impl.camera;

import java.util.LinkedHashSet;
import java.util.Set;

import de.sitl.dev.pov.viewer2.api.camera.CameraChangeListener;
import de.sitl.dev.pov.viewer2.api.camera.CameraChangedEvent;
import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.scene.Scene;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class CameraData implements ReadWritableCamera {
    
    private double x, y, z;
    private double phi, theta;
    private double fOV;
    private double levelOfDetail;
    
    @Getter(AccessLevel.NONE)
    private boolean spotlight;
    
    private Scene scene;
    
    private Set<CameraChangeListener> listeners = new LinkedHashSet<>();

    @Override
    public boolean hasSpotlight() {
        return this.spotlight;
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
        this.scene = scene;
        this.fireStateChanged();
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

}

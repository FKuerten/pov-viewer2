package de.sitl.dev.pov.viewer2.api.camera;

public interface ReadWritableCamera extends ReadableCamera, WritableCamera {
    
    void strafeForward(double forward);
    
    void strafeLeft(double left);
    
    void rotateCW(double deltaPhi);
    
}

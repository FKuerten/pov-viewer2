package de.sitl.dev.pov.viewer2.api.imageSource;

import java.awt.image.BufferedImage;
import java.io.IOException;

import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;

public interface ImageSource {
    
    /**
     * Clears the queue.
     */
    void clearQueue();
    
    ChangingImage
            requestImage(ImmutableCamera camera, final int w, final int h);

}

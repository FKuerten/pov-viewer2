package de.sitl.dev.pov.viewer2.api.imageSource;

import java.awt.image.BufferedImage;

public interface ChangingImage {
    
    BufferedImage getBestImage();
    
    /**
     * @return the originally requested width
     */
    int getRequestedWidth();
    
    /**
     * @return the originally requested height
     */
    int getRequestedHeight();
    
    int getCurrentWidth();
    
    int getCurrentHeight();
    
    void addChangeListener(ImageChangeListener listener);
    
    void removeChangeListener(ImageChangeListener listener);
    
    boolean isAborted();

    void abort();

}

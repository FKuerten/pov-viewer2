package de.sitl.dev.pov.viewer2.api.imageSource;

import java.awt.image.BufferedImage;

/**
 * An image that may change because it is updated in the background.
 * 
 * @author Fabian K&uuml;rten
 */
public interface ChangingImage {
    
    /**
     * @return the best (latest) image
     */
    BufferedImage getBestImage();
    
    /**
     * @return the originally requested width
     */
    int getRequestedWidth();
    
    /**
     * @return the originally requested height
     */
    int getRequestedHeight();
    
    /**
     * @return the width of the currently best image
     */
    int getCurrentWidth();
    
    /**
     * @return the height of the currently best image
     */
    int getCurrentHeight();
    
    /**
     * add an {@link ImageChangeListener}, the listener will be notified
     * whenever a better image is available
     * 
     * @param listener
     *            the listener
     */
    void addChangeListener(ImageChangeListener listener);
    
    /**
     * removes a listener
     * 
     * @param listener
     *            the listener
     */
    void removeChangeListener(ImageChangeListener listener);
    
    /**
     * This is mainly used internally to stop the update thread.
     * 
     * @return whether this changing image has been aborted, it will no longer
     *         generate events
     */
    boolean isAborted();

    /**
     * Stops generating better images.
     */
    void abort();

}

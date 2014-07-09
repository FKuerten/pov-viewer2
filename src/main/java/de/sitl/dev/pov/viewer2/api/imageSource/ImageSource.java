package de.sitl.dev.pov.viewer2.api.imageSource;


import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;

/**
 * Interface for an image source.
 * 
 * @author Fabian K&uuml;rten
 */
public interface ImageSource {
    
    /**
     * Clears the queue.
     */
    void clearQueue();
    
    /**
     * Requests an image from the source. This method will return a reference to
     * a changing image which will asynchronously load the image.
     * 
     * @param camera
     *            what we want to view
     * @param w
     *            specifies the width of the requested image
     * @param h
     *            specifies the height of the requested image
     * @return the asynchronous image loader
     */
    ChangingImage
            requestImage(ImmutableCamera camera, final int w, final int h);

}

package de.sitl.dev.pov.viewer2.impl.imageSource;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.imageSource.ChangingImage;
import de.sitl.dev.pov.viewer2.api.imageSource.ImageSource;

/**
 * Base class for image sources. This class just creates the changing image and
 * provides abstract methods for the actual image generation.
 * 
 * @author Fabian K&uuml;rten
 */
public abstract class AbstractImageSource implements ImageSource {
    
    /**
     * Set of active changing images.
     */
    Set<ChangingImageImplementation> activeImages = Collections
        .newSetFromMap(new WeakHashMap<ChangingImageImplementation, Boolean>());

    @Override
    public ChangingImage requestImage(ImmutableCamera camera, int w, int h) {
        ChangingImageImplementation image =
            new ChangingImageImplementation(this, w, h, camera);
        this.activeImages.add(image);
        return image;
    }
    
    @Override
    public void clearQueue() {
        for (ChangingImageImplementation activeImage : this.activeImages) {
            activeImage.abort();
        }
    }

    /**
     * Synchronously request an image.
     * 
     * @param camera
     *            what and how to view
     * @param w
     *            width of the image
     * @param h
     *            height of the image
     * @return an image
     */
    abstract BufferedImage getImage(ImmutableCamera camera, int w, int h);

    /**
     * Checks whether an image is already generated
     * 
     * @param camera
     *            what and how to view
     * @param w
     *            width of the image
     * @param h
     *            height of the image
     * @return whether the image is in the cache
     */
    abstract boolean hasImageInCache(ImmutableCamera camera, int w, int h);

}

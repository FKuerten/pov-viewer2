package de.sitl.dev.pov.viewer2.impl.imageSource;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import lombok.Data;
import lombok.Getter;
import lombok.Synchronized;
import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.imageSource.ChangingImage;
import de.sitl.dev.pov.viewer2.api.imageSource.ImageChangeListener;

/**
 * The implementation for a changing image. It will try to generate images in
 * increasing qualities with the use of an image source.
 * 
 * @author Fabian K&uuml;rten
 */
public class ChangingImageImplementation implements ChangingImage {
    
    /**
     * The worker.
     * 
     * @author Fabian K&uuml;rten
     */
    class Worker implements Runnable {
        
        @Override
        public void run() {
            ChangingImageImplementation.this.work();
        }
    }
    
    /**
     * The image source for generating the images.
     */
    private final AbstractImageSource source;
    
    /**
     * The requested width of the final image.
     */
    @Getter
    private final int requestedWidth;
    
    /**
     * The requested height of the final image.
     */
    @Getter
    private final int requestedHeight;
    
    /**
     * The width of the current image.
     */
    private int currentWidth = 0;

    /**
     * The height of the current image.
     */
    private int currentHeight = 0;
    
    /**
     * The current image.
     */
    private BufferedImage currentImage;

    /**
     * What we want to display.
     */
    private final ImmutableCamera camera;
    
    /**
     * Set of change listeners.
     */
    private final Set<ImageChangeListener> changeListeners = Collections
        .newSetFromMap(new WeakHashMap<ImageChangeListener, Boolean>());
    
    /**
     * Whether this image has been aborted.
     */
    @Getter
    boolean aborted = false;
    
    /**
     * Constructor
     * 
     * @param source
     *            the image source
     * @param requestedWidth
     *            the final width
     * @param requestedHeight
     *            the final height
     * @param camera
     *            the camera to display
     */
    ChangingImageImplementation(final AbstractImageSource source,
            final int requestedWidth, final int requestedHeight,
            final ImmutableCamera camera) {
        this.source = source;
        this.requestedWidth = requestedWidth;
        this.requestedHeight = requestedHeight;
        this.camera = camera;
        Thread thread = new Thread(new Worker());
        thread.start();
    }
    
    @Override
    public void addChangeListener(ImageChangeListener listener) {
        this.changeListeners.add(listener);
    }
    
    @Override
    public void removeChangeListener(ImageChangeListener listener) {
        this.changeListeners.remove(listener);
    }
    
    /**
     * Notify listeners that we have a new image.
     */
    private void fireChanged() {
        for (ImageChangeListener changeListener : this.changeListeners) {
            changeListener.imageChanged();
        }
    }
    
    /**
     * Synchronization lock.
     */
    private final Object[] lock = new Object[0];

    /**
     * Helper class for <strong>w</strong>idth and <strong>h</strong>eight.
     * 
     * @author Fabian K&uuml;rten
     */
    @Data
    private static class WH {
        /**
         * Sizes.
         */
        public final int w, h;
        
        /**
         * @return an instance with half the width and height
         */
        public WH half() {
            final int nw = this.w > 1 ? this.w / 2 : 1;
            final int nh = this.h > 1 ? this.h / 2 : 1;
            return new WH(nw, nh);
        }
        
        /**
         * @return whether this is already the smallest value possible
         */
        public boolean isSmallest() {
            return this.w == 1 && this.h == 1;
        }

        @SuppressWarnings("boxing")
        @Override
        public String toString() {
            return String.format("(%d,%d)", this.w, this.h);
        }
    }
    
    /**
     * The actual work method
     */
    void work() {
        // Generate progression
        final List<WH> sizes = new ArrayList<>();
        WH current = new WH(this.requestedWidth, this.requestedHeight);
        while (true) {
            sizes.add(0, current);
            if (this.source.hasImageInCache(this.camera, current.w, current.h)) {
                // image available, break
                break;
            }
            if (current.isSmallest()) {
                break;
            }
            current = current.half();
        }
        
        // Actually generate the images.
        for (WH size : sizes) {
            if (this.isAborted()) {
                break;
            }
            BufferedImage image =
                this.source.getImage(this.camera, size.w, size.h);
            synchronized (this.lock) {
                this.currentImage = image;
                this.currentWidth = size.w;
                this.currentHeight = size.h;
            }
            this.fireChanged();
        }
        
    }
    
    @Override
    @Synchronized("lock")
    public int getCurrentWidth() {
        return this.currentWidth;
    }
    
    @Override
    @Synchronized("lock")
    public int getCurrentHeight() {
        return this.currentHeight;
    }

    @Override
    @Synchronized("lock")
    public BufferedImage getBestImage() {
        return this.currentImage;
    }
    
    /**
     * @return whether this one is finished
     */
    public boolean isFinished() {
        return this.getCurrentWidth() == this.getRequestedWidth()
            && this.getCurrentHeight() == this.getRequestedHeight();
    }

    @Override
    public void abort() {
        this.aborted = true;
    }

}

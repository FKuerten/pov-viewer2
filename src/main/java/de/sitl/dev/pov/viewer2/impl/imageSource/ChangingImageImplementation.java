package de.sitl.dev.pov.viewer2.impl.imageSource;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Synchronized;
import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.imageSource.ChangingImage;
import de.sitl.dev.pov.viewer2.api.imageSource.ImageChangeListener;

public class ChangingImageImplementation implements ChangingImage {
    
    class Worker implements Runnable {
        
        @Override
        public void run() {
            ChangingImageImplementation.this.work();
        }
    }
    
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

    private final ImmutableCamera camera;
    
    private final Set<ImageChangeListener> changeListeners =
        new LinkedHashSet<>();
    
    @Getter
    boolean aborted = false;
    
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
    
    private void fireChanged() {
        for (ImageChangeListener changeListener : this.changeListeners) {
            changeListener.imageChanged();
        }
    }
    
    private final Object[] lock = new Object[0];

    @Data
    private static class WH {
        public final int w, h;
        
        public WH half() {
            final int nw = this.w > 1 ? this.w / 2 : 1;
            final int nh = this.h > 1 ? this.h / 2 : 1;
            return new WH(nw, nh);
        }
        
        public boolean isSmallest() {
            return this.w == 1 && this.h == 1;
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", this.w, this.h);
        }
    }
    
    void work() {
        // Generate progression
        final List<WH> sizes = new ArrayList<>();
        WH current = new WH(this.requestedWidth, this.requestedHeight);
        while (true) {
            sizes.add(0, current);
            if (this.source.hasImage(this.camera, current.w, current.h)) {
                // image available, break
                break;
            }
            if (current.isSmallest()) {
                break;
            }
            current = current.half();
        }
        
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
    
    public boolean isFinished() {
        return this.getCurrentWidth() == this.getRequestedWidth()
            && this.getCurrentHeight() == this.getRequestedHeight();
    }

    public void abort() {
        this.aborted = true;
    }

}

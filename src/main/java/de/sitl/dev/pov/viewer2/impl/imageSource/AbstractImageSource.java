package de.sitl.dev.pov.viewer2.impl.imageSource;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakHashMap;

import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.imageSource.ChangingImage;
import de.sitl.dev.pov.viewer2.api.imageSource.ImageSource;

public abstract class AbstractImageSource implements ImageSource {
    
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

    abstract BufferedImage getImage(ImmutableCamera camera, int w, int h);

    abstract boolean hasImage(ImmutableCamera camera, int w, int h);

}

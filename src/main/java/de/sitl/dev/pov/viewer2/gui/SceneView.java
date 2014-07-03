package de.sitl.dev.pov.viewer2.gui;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import lombok.RequiredArgsConstructor;
import de.sitl.dev.pov.viewer2.api.camera.CameraChangeListener;
import de.sitl.dev.pov.viewer2.api.camera.CameraChangedEvent;
import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.imageSource.ChangingImage;
import de.sitl.dev.pov.viewer2.api.imageSource.ImageChangeListener;
import de.sitl.dev.pov.viewer2.api.imageSource.ImageSource;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadableRoundingCamera;

public class SceneView extends JPanel {
    
    class CameraChangeListenerImplementation implements CameraChangeListener {
        @Override
        public void stateChanged(CameraChangedEvent event) {
            SceneView.this.resetImage();
        }
    }
    
    class ImageChangeListenerImplementation implements ImageChangeListener {

        @Override
        public void imageChanged() {
            SceneView.this.updateImage();
        }
        
    }
    
    class PanelComponentListener extends ComponentAdapter {
        
        @Override
        public void componentResized(ComponentEvent e) {
            SceneView.this.validate();
            SceneView.this.resetImage();
        }
        
    }

    private final ReadWritableCamera camera;
    
    private final ImageSource imageSource;
    
    private final CameraChangeListener cameraChangeListener =
        new CameraChangeListenerImplementation();
    
    private final ImageChangeListener imageChangeListener =
        new ImageChangeListenerImplementation();
    
    private final PanelComponentListener componentListener =
        new PanelComponentListener();

    private ChangingImage changingImage;
    private ReadableRoundingCamera roundingCamera;

    private BufferedImage image;
    
    public SceneView(ReadWritableCamera camera,
            ReadableRoundingCamera roundingCamera, ImageSource imageSource) {
        this.camera = camera;
        this.roundingCamera = roundingCamera;
        this.imageSource = imageSource;
        this.camera.addChangeListener(this.cameraChangeListener);
        this.addComponentListener(this.componentListener);
        this.resetImage();
    }
    
    void resetImage() {
        if (this.changingImage != null) {
            this.changingImage.abort();
            this.changingImage = null;
        }
        this.image = null;
        final ImmutableCamera immutableCamera =
            this.roundingCamera.getAsImmutableCamera();
        final int viewW = this.getWidth();
        final int viewH = this.getHeight();
        if (viewW > 0 && viewH > 0) {
            this.changingImage =
                this.imageSource.requestImage(immutableCamera, viewW, viewH);
            this.changingImage.addChangeListener(this.imageChangeListener);
            this.updateImage();
        }
    }
    
    void updateImage() {
        final BufferedImage newImage = this.changingImage.getBestImage();
        if (newImage != this.image) {
            this.image = newImage;
            this.repaint();
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        if (this.image != null) {
            final int viewW = this.getWidth();
            final int viewH = this.getHeight();
            g.drawImage(this.image, 0, 0, viewW, viewH, null);
        }
    }
}

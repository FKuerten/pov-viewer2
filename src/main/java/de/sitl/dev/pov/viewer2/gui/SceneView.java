package de.sitl.dev.pov.viewer2.gui;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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
            SceneView.this.repaint();
        }
        
    }
    
    class PanelComponentListener extends ComponentAdapter {
        
        @Override
        public void componentResized(ComponentEvent e) {
            SceneView.this.validate();
            SceneView.this.resetImage();
        }
    }
    
    class KeyListenerImplementation extends KeyAdapter {
        
        @SuppressWarnings("boxing")
        @Override
        public void keyPressed(KeyEvent e) {
            SceneView.this.keys.add(e.getKeyCode());
        }
        
        @SuppressWarnings("boxing")
        @Override
        public void keyReleased(KeyEvent e) {
            SceneView.this.keys.remove(e.getKeyCode());
        }
        
    }
    
    class MouseListenerImplementation extends MouseAdapter {
        
        @Override
        public void mouseClicked(MouseEvent e) {
            SceneView.this.requestFocusInWindow();
        }
        
    }

    class KeyRunnable implements Runnable {
        
        @SuppressWarnings("boxing")
        @Override
        public void run() {
            final Set<Integer> keys = SceneView.this.keys;
            final ReadWritableCamera camera = SceneView.this.camera;
            long lastTime = System.currentTimeMillis();
            while (true) {
                long current = System.currentTimeMillis();
                long delta = current - lastTime;
                double dt = delta / 1000d;
                final boolean forward = keys.contains(KeyEvent.VK_W);
                final boolean backward = keys.contains(KeyEvent.VK_S);
                if (forward && !backward) {
                    camera.strafeForward(1 * dt);
                } else if (!forward && backward) {
                    camera.strafeForward(-1 * dt);
                }
                final boolean strafeLeft = keys.contains(KeyEvent.VK_A);
                final boolean strafeRight = keys.contains(KeyEvent.VK_D);
                if (strafeLeft && !strafeRight) {
                    camera.strafeLeft(1 * dt);
                } else if (!strafeLeft && strafeRight) {
                    camera.strafeLeft(-1 * dt);
                }
                final boolean lookLeft = keys.contains(KeyEvent.VK_LEFT);
                final boolean lookRight = keys.contains(KeyEvent.VK_RIGHT);
                if (lookLeft && !lookRight) {
                    camera.rotateCW(15 * dt);
                } else if (!lookLeft && lookRight) {
                    camera.rotateCW(-15 * dt);
                }
                
                lastTime = current;
                if (!SceneView.this.active) {
                    break;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // huh?
                }
            }
        }
    }

    private final CameraChangeListener cameraChangeListener =
        new CameraChangeListenerImplementation();
    
    private final ImageChangeListener imageChangeListener =
        new ImageChangeListenerImplementation();
    
    private final PanelComponentListener componentListener =
        new PanelComponentListener();
    
    private final KeyListener keyListener = new KeyListenerImplementation();
    
    private final MouseListener mouseListener =
        new MouseListenerImplementation();

    Set<Integer> keys = Collections.synchronizedSet(new HashSet<Integer>());
    
    private final Thread keyThread;

    final ReadWritableCamera camera;
    
    private final ImageSource imageSource;

    private ChangingImage changingImage;
    private ReadableRoundingCamera roundingCamera;
    
    private final Object[] imageLock = new Object[0];
    
    boolean active = true;

    public SceneView(ReadWritableCamera camera,
            ReadableRoundingCamera roundingCamera, ImageSource imageSource) {
        this.camera = camera;
        this.roundingCamera = roundingCamera;
        this.imageSource = imageSource;
        this.camera.addChangeListener(this.cameraChangeListener);
        this.addComponentListener(this.componentListener);
        this.addKeyListener(this.keyListener);
        this.setFocusable(true);
        this.addMouseListener(this.mouseListener);
        this.keyThread = new Thread(new KeyRunnable());
        this.keyThread.start();
        this.resetImage();
    }
    
    void resetImage() {
        final ImmutableCamera immutableCamera =
            this.roundingCamera.getAsImmutableCamera();
        final int viewW = this.getWidth();
        final int viewH = this.getHeight();
        if (viewW > 0 && viewH > 0) {
            ChangingImage newChangingImage =
                this.imageSource.requestImage(immutableCamera, viewW, viewH);
            newChangingImage.addChangeListener(this.imageChangeListener);
            synchronized (this.imageLock) {
                if (this.changingImage != null) {
                    this.changingImage.abort();
                    this.changingImage = null;
                }
                this.changingImage = newChangingImage;
            }
            this.repaint();
        }
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        synchronized (this.imageLock) {
            if (this.changingImage != null) {
                final BufferedImage newImage =
                    this.changingImage.getBestImage();
                if (newImage != null) {
                    final int viewW = this.getWidth();
                    final int viewH = this.getHeight();
                    g.drawImage(newImage, 0, 0, viewW, viewH, null);
                }
            }
        }
    }
}

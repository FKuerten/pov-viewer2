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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import de.sitl.dev.pov.viewer2.api.camera.CameraChangeListener;
import de.sitl.dev.pov.viewer2.api.camera.CameraChangedEvent;
import de.sitl.dev.pov.viewer2.api.camera.ImmutableCamera;
import de.sitl.dev.pov.viewer2.api.camera.ReadWritableCamera;
import de.sitl.dev.pov.viewer2.api.imageSource.ChangingImage;
import de.sitl.dev.pov.viewer2.api.imageSource.ImageChangeListener;
import de.sitl.dev.pov.viewer2.api.imageSource.ImageSource;
import de.sitl.dev.pov.viewer2.api.roundingcamera.ReadableRoundingCamera;

/**
 * This panel display the rendered images. It also receives keyboard events and
 * moves the camera.
 * 
 * @author Fabian K&uuml;rten
 */
public class SceneView extends JPanel {
    
    /**
     * Listens for camera changes and resets the image.
     * 
     * @author Fabian K&uuml;rten
     */
    class CameraChangeListenerImplementation implements CameraChangeListener {
        @Override
        public void cameraChanged(CameraChangedEvent event) {
            SceneView.this.resetImage();
        }
    }
    
    /**
     * Listens for image changes and repaints the view.
     * 
     * @author Fabian K&uuml;rten
     */
    class ImageChangeListenerImplementation implements ImageChangeListener {

        @Override
        public void imageChanged() {
            SceneView.this.repaint();
        }
        
    }
    
    /**
     * Listens for resize events and reset the image.
     * 
     * @author Fabian K&uuml;rten
     */
    class PanelComponentListener extends ComponentAdapter {
        
        @Override
        public void componentResized(ComponentEvent e) {
            SceneView.this.validate();
            SceneView.this.resetImage();
        }
    }
    
    /**
     * Listens for keyboard events and updates they keyboard state.
     * 
     * @author Fabian K&uuml;rten
     */
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
    
    /**
     * Listens for mouse events and updates focus.
     * 
     * @author Fabian K&uuml;rten
     */
    class MouseListenerImplementation extends MouseAdapter {
        
        @Override
        public void mouseClicked(MouseEvent e) {
            SceneView.this.requestFocusInWindow();
        }
        
    }

    /**
     * A runnable to move the camera.
     * 
     * @author Fabian K&uuml;rten
     */
    class KeyRunnable implements Runnable {
        
        @SuppressWarnings("boxing")
        @Override
        public void run() {
            // short local names for keys and camera
            @SuppressWarnings("hiding")
            final Set<Integer> keys = SceneView.this.keys;
            @SuppressWarnings("hiding")
            final ReadWritableCamera camera = SceneView.this.camera;
            
            // last update time, initially the start time
            long lastTime = System.currentTimeMillis();
            while (true) {
                long current = System.currentTimeMillis();
                long delta = current - lastTime; // in ms
                // dt determines how far we move the camera
                double dt = delta / 1000d; // in s
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
                    // ignored
                }
            }
        }
    }

    /**
     * The actual camera change listener.
     */
    private final CameraChangeListener cameraChangeListener =
        new CameraChangeListenerImplementation();
    
    /**
     * The actual image change listener.
     */
    private final ImageChangeListener imageChangeListener =
        new ImageChangeListenerImplementation();
    
    /**
     * The actual resize listener.
     */
    private final PanelComponentListener componentListener =
        new PanelComponentListener();
    
    /**
     * The actual keyboard listener.
     */
    private final KeyListener keyListener = new KeyListenerImplementation();
    
    /**
     * The actual mouse listener.
     */
    private final MouseListener mouseListener =
        new MouseListenerImplementation();

    /**
     * The set of keys currently pressed keys.
     */
    Set<Integer> keys = Collections.synchronizedSet(new HashSet<Integer>());
    
    /**
     * They keyboard thread, responsible to moving the camera.
     */
    private final Thread keyThread;

    /**
     * The camera we move.
     */
    final ReadWritableCamera camera;
    
    /**
     * The image source.
     */
    private final ImageSource imageSource;

    /**
     * The currently active image.
     */
    private ChangingImage changingImage;
    
    /**
     * The camera we display.
     */
    private final ReadableRoundingCamera roundingCamera;

    /**
     * A lock for synchronization.
     */
    private final Object[] imageLock = new Object[0];
    
    /**
     * Whether we are still active.
     */
    boolean active = true;

    /**
     * The constructor. Sets fields, creates listeners, starts the key thread
     * and resets the image.
     * 
     * @param camera
     *            the camera to move
     * @param roundingCamera
     *            the camera to display
     * @param imageSource
     *            the image source
     */
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
    
    /**
     * Resets the image. Should be called whenever the camera moved or the
     * viewport changed.
     */
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

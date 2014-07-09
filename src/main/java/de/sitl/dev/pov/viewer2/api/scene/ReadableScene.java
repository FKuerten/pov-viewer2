package de.sitl.dev.pov.viewer2.api.scene;

/**
 * Allows reading the scene's properties.
 * 
 * @author Fabian K&uuml;rten
 */
public interface ReadableScene extends Scene {
    
    /**
     * @return an immutable version of this scene
     */
    ImmutableScene getAsImmutableScene();
    
    /**
     * @return an identifier for this scene
     */
    String getName();
}

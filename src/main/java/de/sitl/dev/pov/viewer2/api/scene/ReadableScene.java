package de.sitl.dev.pov.viewer2.api.scene;

public interface ReadableScene extends Scene {
    ImmutableScene getAsImmutableScene();
    
    /**
     * @return an identifier for this scene
     */
    String getName();
}

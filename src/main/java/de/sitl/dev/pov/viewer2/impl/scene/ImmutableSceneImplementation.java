package de.sitl.dev.pov.viewer2.impl.scene;

import lombok.Getter;
import de.sitl.dev.pov.viewer2.api.scene.ImmutableScene;
import de.sitl.dev.pov.viewer2.api.scene.ReadableScene;

/**
 * An immutable scene.
 * 
 * @author Fabian K&uuml;rten
 */
public class ImmutableSceneImplementation implements ImmutableScene {
    
    /**
     * The scene's name.
     */
    @Getter
    private final String name;
    
    /**
     * Copy constructor
     * 
     * @param scene
     *            original scene
     */
    public ImmutableSceneImplementation(ReadableScene scene) {
        this.name = scene.getName();
    }
    
    @Override
    public ImmutableScene getAsImmutableScene() {
        return this;
    }

}

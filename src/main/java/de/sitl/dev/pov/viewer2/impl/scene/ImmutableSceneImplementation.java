package de.sitl.dev.pov.viewer2.impl.scene;

import lombok.Getter;
import de.sitl.dev.pov.viewer2.api.scene.ImmutableScene;
import de.sitl.dev.pov.viewer2.api.scene.ReadableScene;

public class ImmutableSceneImplementation implements ImmutableScene {
    
    @Getter
    private final String name;
    
    public ImmutableSceneImplementation(ReadableScene scene) {
        this.name = scene.getName();
    }
    
    @Override
    public ImmutableScene getAsImmutableScene() {
        return this;
    }

}

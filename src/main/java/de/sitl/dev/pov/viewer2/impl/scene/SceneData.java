package de.sitl.dev.pov.viewer2.impl.scene;

import lombok.AllArgsConstructor;
import lombok.Getter;
import de.sitl.dev.pov.viewer2.api.scene.ImmutableScene;
import de.sitl.dev.pov.viewer2.api.scene.ReadableScene;

@AllArgsConstructor
public class SceneData implements ReadableScene {
    
    @Getter
    private String name;
    
    @Override
    public ImmutableScene getAsImmutableScene() {
        return new ImmutableSceneImplementation(this);
    }
    
}

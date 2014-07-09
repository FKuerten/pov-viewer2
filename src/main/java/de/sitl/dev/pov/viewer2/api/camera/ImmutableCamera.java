package de.sitl.dev.pov.viewer2.api.camera;

import de.sitl.dev.pov.viewer2.api.scene.ImmutableScene;

/**
 * A camera that can <em>not</em> be changed.
 * 
 * @author Fabian K&uuml;rten
 */
public interface ImmutableCamera extends ReadableCamera {
    
    /**
     * Override of {@link ReadableCamera#getScene()} to indicate that an
     * immutable camera must have an {@link ImmutableScene}.
     */
    @Override
    ImmutableScene getScene();

}

package dev.isxander.yaul3.api.animation;

import org.jetbrains.annotations.ApiStatus;

public interface Animatable {
    Animatable play();

    @ApiStatus.Internal
    void tick(float tickDelta);

    void skipToEnd();

    boolean hasStarted();
    boolean isDone();
    boolean isPlaying();

    Animatable copy();
}

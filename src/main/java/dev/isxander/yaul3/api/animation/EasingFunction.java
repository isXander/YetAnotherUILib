package dev.isxander.yaul3.api.animation;

public interface EasingFunction {
    float ease(float t);

    EasingFunction LINEAR = t -> t;
}

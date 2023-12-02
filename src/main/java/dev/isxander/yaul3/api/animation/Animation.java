package dev.isxander.yaul3.api.animation;

import dev.isxander.yaul3.impl.animation.AnimationImpl;

import java.util.function.Consumer;

public interface Animation extends Animatable {
    static Animation of(int durationTicks) {
        return new AnimationImpl().duration(durationTicks);
    }

    Animation consumerI(Consumer<Integer> consumer, double start, double end);
    Animation consumerF(Consumer<Float> consumer, double start, double end);
    Animation consumerD(Consumer<Double> consumer, double start, double end);

    Animation deltaConsumerI(Consumer<Integer> consumer, double start, double end);
    Animation deltaConsumerF(Consumer<Float> consumer, double start, double end);
    Animation deltaConsumerD(Consumer<Double> consumer, double start, double end);

    Animation duration(int ticks);
    Animation easing(EasingFunction easing);

    @Override
    Animation copy();

    @Override
    Animation play();
}

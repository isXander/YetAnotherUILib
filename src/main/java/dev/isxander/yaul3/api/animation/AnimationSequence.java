package dev.isxander.yaul3.api.animation;

import dev.isxander.yaul3.impl.animation.AnimationSequenceImpl;

public interface AnimationSequence extends Animatable {
    static AnimationSequence of() {
        return new AnimationSequenceImpl();
    }

    static AnimationSequence of(Animatable... animation) {
        return of().push(animation);
    }

    AnimationSequence push(Animatable... animation);

    @Override
    AnimationSequence play();

    @Override
    AnimationSequence copy();
}

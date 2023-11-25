package dev.isxander.yaul3.api.animation;

import dev.isxander.yaul3.impl.animation.AnimationGroupImpl;

public interface AnimationGroup extends Animatable {
    static AnimationGroup of() {
        return new AnimationGroupImpl();
    }

    static AnimationGroup of(Animatable... animation) {
        return of().add(animation);
    }

    AnimationGroup add(Animatable... animation);

    @Override
    AnimationGroup play();

    @Override
    AnimationGroup copy();
}

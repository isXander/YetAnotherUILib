package dev.isxander.yaul3.impl.animation;

import dev.isxander.yaul3.api.animation.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

public final class Animator {
    public static final Animator INSTANCE = new Animator();

    private final List<Animatable> animatables;

    private Animator() {
        this.animatables = new ObjectArrayList<>();
    }

    public void add(Animatable animatable) {
        animatables.add(animatable);
    }

    public void tick(float tickDelta) {
        animatables.removeIf(animatable -> {
            animatable.tick(tickDelta);
            return animatable.isDone();
        });
    }
}

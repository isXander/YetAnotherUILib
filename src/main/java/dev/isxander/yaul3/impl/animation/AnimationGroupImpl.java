package dev.isxander.yaul3.impl.animation;

import dev.isxander.yaul3.api.animation.Animatable;
import dev.isxander.yaul3.api.animation.AnimationGroup;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Set;

public class AnimationGroupImpl implements AnimationGroup {
    private final Set<Animatable> animatables;

    private boolean started, done;

    public AnimationGroupImpl() {
        this.animatables = new ObjectArraySet<>();
    }

    @Override
    public AnimationGroup add(Animatable... animation) {
        for (Animatable animatable : animation)
            Validate.isTrue(!animatable.hasStarted(), "Cannot add an animation that has already started!");

        animatables.addAll(List.of(animation));
        return this;
    }

    @Override
    public void tick(float tickDelta) {
        if (done) return;

        started = true;
        // this should not be allMatch, because it will terminate when it sees the first false
        done = !animatables.stream().noneMatch(animatable -> {
            animatable.tick(tickDelta);
            return animatable.isDone();
        });
    }

    @Override
    public void skipToEnd() {
        animatables.forEach(Animatable::skipToEnd);
        done = true;
        started = true;
    }

    @Override
    public void stopNow() {
        animatables.forEach(Animatable::stopNow);
        done = true;
        started = true;
    }

    @Override
    public boolean hasStarted() {
        return started;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean isPlaying() {
        return started && !done;
    }

    @Override
    public AnimationGroup play() {
        Animator.INSTANCE.add(this);
        return this;
    }

    @Override
    public AnimationGroup copy() {
        AnimationGroup group = AnimationGroup.of();
        animatables.forEach(animatable -> group.add(animatable.copy()));

        return group;
    }
}

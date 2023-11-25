package dev.isxander.yaul3.impl.animation;

import dev.isxander.yaul3.api.animation.Animatable;
import dev.isxander.yaul3.api.animation.AnimationSequence;
import org.apache.commons.lang3.Validate;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class AnimationSequenceImpl implements AnimationSequence {
    private final Queue<Animatable> queue;
    private Animatable current;

    private boolean started;
    private boolean done;

    public AnimationSequenceImpl() {
        this.queue = new ArrayDeque<>();
        this.current = null;
    }

    @Override
    public AnimationSequence push(Animatable... animation) {
        for (Animatable animatable : animation)
            Validate.isTrue(!animatable.hasStarted(), "Cannot add an animation that has already started!");

        queue.addAll(List.of(animation));
        return this;
    }

    @Override
    public void tick(float tickDelta) {
        if (current == null) {
            current = queue.poll();
            if (current != null) {
                current.play();
                started = true;
            } else {
                done = true;
            }
        } else {
            current.tick(tickDelta);
            if (current.isDone()) {
                current = null;
            }
        }
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
    public AnimationSequence play() {
        Animator.INSTANCE.add(this);
        return this;
    }

    @Override
    public void skipToEnd() {
        while (current != null) {
            current.skipToEnd();
            current = queue.poll();
        }
        done = true;
    }

    @Override
    public AnimationSequence copy() {
        Validate.isTrue(!started, "Cannot copy an animation sequence that has already started.");

        AnimationSequence sequence = AnimationSequence.of();
        queue.forEach(animation -> sequence.push(animation.copy()));
        return sequence;
    }
}

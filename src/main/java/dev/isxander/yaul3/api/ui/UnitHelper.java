package dev.isxander.yaul3.api.ui;

import dev.isxander.yaul3.impl.ui.state.IntStateImpl;
import dev.isxander.yaul3.mixins.layout.ScreenAccessor;
import net.minecraft.client.gui.screens.Screen;

/**
 * Should be implemented on a Screen, or anything that can provide a screen with {@link #unitScreen()}.
 */
public interface UnitHelper {
    default Screen unitScreen() {
        return (Screen) this;
    }

    default IntState zero() {
        return new IntStateImpl(0);
    }

    default IntState px(int px) {
        return new IntStateImpl(px);
    }

    default IntState rem(float rem) {
        return new IntStateImpl((int) (rem * fontSize().get()));
    }

    default IntState percentW(int percent) {
        return new IntStateImpl((int) (percent * unitScreen().width / 100f));
    }

    default IntState percentH(int percent) {
        return new IntStateImpl((int) (percent * unitScreen().height / 100f));
    }

    default IntState buttonHeight(float buttonHeight) {
        return new IntStateImpl((int) (20 * buttonHeight));
    }

    default IntState fontSize() {
        return new IntStateImpl(((ScreenAccessor) unitScreen()).getFont().lineHeight);
    }
}

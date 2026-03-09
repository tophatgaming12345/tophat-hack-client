package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;

public class Fly extends Module {

    public Fly() {
        super("Fly", "Allows you to fly like creative mode.", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.player != null) {
            mc.player.getAbilities().flying = true;
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.getAbilities().flying = false;
        }
    }
}

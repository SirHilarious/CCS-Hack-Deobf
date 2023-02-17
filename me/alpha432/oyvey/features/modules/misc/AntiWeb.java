//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import me.alpha432.oyvey.event.events.*;
import me.alpha432.oyvey.util.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AntiWeb extends Module
{
    private Setting<Float> speed;
    
    public AntiWeb() {
        super("AntiWeb", "Stops you being slowed down by webs", Category.MISC, true, false, false);
        this.speed = (Setting<Float>)this.register(new Setting("Factor", (T)1.0f, (T)1.0f, (T)10.0f));
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            return;
        }
        if (AntiWeb.mc.player.isInWeb) {
            final double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
            AntiWeb.mc.player.motionX = calc[0];
            AntiWeb.mc.player.motionZ = calc[1];
            if (AntiWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                AntiWeb.mc.player.motionY -= this.speed.getValue() / 10.0f;
            }
        }
    }
}

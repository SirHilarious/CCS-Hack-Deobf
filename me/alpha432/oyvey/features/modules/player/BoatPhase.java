//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.*;
import net.minecraft.entity.item.*;

public class BoatPhase extends Module
{
    public BoatPhase() {
        super("BoatPhase", "Allows you to phase while in a boat", Module.Category.PLAYER, true, false, false);
    }
    
    public void onUpdate() {
        if (!(BoatPhase.mc.player.ridingEntity instanceof EntityBoat)) {
            this.disable();
            return;
        }
        BoatPhase.mc.player.ridingEntity.noClip = true;
        BoatPhase.mc.player.ridingEntity.setNoGravity(true);
        BoatPhase.mc.player.noClip = true;
    }
}

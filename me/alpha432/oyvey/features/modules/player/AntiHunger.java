//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AntiHunger extends Module
{
    private boolean onGround;
    
    public AntiHunger() {
        super("AntiHunger", "Stops you from getting hungry", Module.Category.PLAYER, true, false, false);
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0) {
            this.onGround = AntiHunger.mc.player.onGround;
            if (AntiHunger.mc.player.posY == AntiHunger.mc.player.prevPosY) {
                AntiHunger.mc.player.onGround = false;
            }
        }
        else {
            AntiHunger.mc.player.onGround = this.onGround;
        }
    }
}

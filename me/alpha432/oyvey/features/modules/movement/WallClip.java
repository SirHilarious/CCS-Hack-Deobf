//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class WallClip extends Module
{
    public WallClip() {
        super("WallClip", "Go LARPer mode on a nigga", Module.Category.MOVEMENT, true, false, false);
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            return;
        }
        if (WallClip.mc.player.collidedHorizontally && WallClip.mc.gameSettings.keyBindForward.isKeyDown()) {
            WallClip.mc.player.noClip = true;
            WallClip.mc.player.motionY = 0.0;
            WallClip.mc.player.motionX = 0.0;
            WallClip.mc.player.motionZ = 0.0;
            WallClip.mc.player.onGround = false;
            WallClip.mc.player.collidedHorizontally = false;
            WallClip.mc.player.capabilities.isFlying = false;
            WallClip.mc.player.collidedVertically = false;
            final double x = Math.cos(Math.toRadians(WallClip.mc.player.rotationYaw + 90.0f));
            final double z = Math.sin(Math.toRadians(WallClip.mc.player.rotationYaw + 90.0f));
            WallClip.mc.player.setPosition(WallClip.mc.player.posX + (0.9 * x + 0.0 * z), WallClip.mc.player.posY, WallClip.mc.player.posZ + (0.9 * z - 0.0 * x));
        }
    }
}

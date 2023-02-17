//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import me.alpha432.oyvey.event.events.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class NoClip extends Module
{
    private Setting<Integer> factorAmt;
    private Setting<Float> inherit;
    
    public NoClip() {
        super("NoClip", "Allows you to clip thru walls", Module.Category.MOVEMENT, true, false, false);
        this.factorAmt = (Setting<Integer>)this.register(new Setting("Factor", (T)5, (T)1, (T)10));
        this.inherit = (Setting<Float>)this.register(new Setting("Inheritance", (T)1.0f, (T)1.0f, (T)5.0f));
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            return;
        }
        NoClip.mc.player.setVelocity(0.0, 0.0, 0.0);
        NoClip.mc.player.noClip = true;
        NoClip.mc.player.motionY = 0.0;
        NoClip.mc.player.motionX = 0.0;
        NoClip.mc.player.motionZ = 0.0;
        NoClip.mc.player.onGround = false;
        NoClip.mc.player.collidedHorizontally = false;
        NoClip.mc.player.capabilities.isFlying = false;
        NoClip.mc.player.collidedVertically = false;
        final double[] strafing = MathUtil.directionSpeed((NoClip.mc.player.ticksExisted % 20 == 0) ? (0.03 + this.inherit.getValue() / 100.0f) : (0.07 + this.inherit.getValue() / 100.0f));
        float motionY = 0.0f;
        if (NoClip.mc.gameSettings.keyBindJump.isKeyDown()) {
            motionY = 0.0015f;
        }
        if (NoClip.mc.gameSettings.keyBindSneak.isKeyDown() && NoClip.mc.player.posY > 1.0) {
            motionY = -0.0015f;
        }
        for (int n2 = this.factorAmt.getValue(); n2 >= 1; --n2) {
            NoClip.mc.player.motionX = strafing[0] / n2;
            NoClip.mc.player.motionY = motionY;
            NoClip.mc.player.motionZ = strafing[1] / n2;
            NoClip.mc.player.setPosition(NoClip.mc.player.posX + NoClip.mc.player.motionX, NoClip.mc.player.posY + NoClip.mc.player.motionY, NoClip.mc.player.posZ + NoClip.mc.player.motionZ);
            NoClip.mc.getConnection().sendPacket((Packet)new CPacketConfirmTeleport());
        }
    }
}

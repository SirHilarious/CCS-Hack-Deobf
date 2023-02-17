//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.event.events.*;
import me.alpha432.oyvey.features.modules.movement.*;
import net.minecraft.network.play.server.*;

public class Velocity extends Module
{
    public Velocity() {
        super("Velocity", "Stops you from taking knockback", Module.Category.PLAYER, true, false, false);
    }
    
    @SubscribeEvent
    public void onPushOutOfBlocks(final PushEvent event) {
        if (event.getStage() == 1) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityVelocity && !PacketFly.fullNullCheck()) {
            final SPacketEntityVelocity packet = (SPacketEntityVelocity)event.getPacket();
            if (packet.entityID == Velocity.mc.player.entityId) {
                packet.motionX = 0;
                packet.motionY = 0;
                packet.motionZ = 0;
            }
        }
        if (event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion packet2 = (SPacketExplosion)event.getPacket();
            packet2.motionX = 0.0f;
            packet2.motionY = 0.0f;
            packet2.motionZ = 0.0f;
        }
    }
}

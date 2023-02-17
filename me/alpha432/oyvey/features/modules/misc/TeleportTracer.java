//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.features.command.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class TeleportTracer extends Module
{
    public TeleportTracer() {
        super("TeleportTracer", "Tracks players when they teleport", Category.MISC, true, false, false);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityTeleport) {
            final SPacketEntityTeleport sPacketEntityTeleport = (SPacketEntityTeleport)event.getPacket();
            if (TeleportTracer.mc.world.getEntityByID(sPacketEntityTeleport.getEntityId()) instanceof EntityPlayer && TeleportTracer.mc.world.getEntityByID(sPacketEntityTeleport.getEntityId()).getDistance((Entity)TeleportTracer.mc.player) > 50.0f) {
                Command.sendMessage("Teleport Detected: " + TeleportTracer.mc.world.getEntityByID(sPacketEntityTeleport.getEntityId()).getName() + " X: " + sPacketEntityTeleport.getX() + " Y: " + sPacketEntityTeleport.getY() + " Z: " + sPacketEntityTeleport.getZ());
            }
        }
    }
}

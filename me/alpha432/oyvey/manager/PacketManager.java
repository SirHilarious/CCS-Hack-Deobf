//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.*;
import net.minecraft.network.*;
import java.util.*;

public class PacketManager extends Feature
{
    private final List<Packet<?>> noEventPackets;
    
    public PacketManager() {
        this.noEventPackets = new ArrayList<Packet<?>>();
    }
    
    public void sendPacketNoEvent(final Packet<?> packet) {
        if (packet != null && !nullCheck()) {
            this.noEventPackets.add(packet);
            PacketManager.mc.player.connection.sendPacket((Packet)packet);
        }
    }
    
    public boolean shouldSendPacket(final Packet<?> packet) {
        if (this.noEventPackets.contains(packet)) {
            this.noEventPackets.remove(packet);
            return false;
        }
        return true;
    }
}

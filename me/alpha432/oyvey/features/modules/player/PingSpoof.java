//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.network.*;
import java.util.concurrent.*;
import java.util.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class PingSpoof extends Module
{
    private Setting<Integer> waitTime;
    private Setting<Boolean> keepAlive;
    private Setting<Boolean> confirmTransaction;
    private Timer timer;
    private Queue<Packet<?>> packetList;
    private boolean blocking;
    
    public PingSpoof() {
        super("PingSpoof", "Makes your ping higher", Module.Category.PLAYER, true, false, false);
        this.waitTime = (Setting<Integer>)this.register(new Setting("Delay", (T)150, (T)1, (T)2500));
        this.keepAlive = (Setting<Boolean>)this.register(new Setting("KeepAlive", (T)true));
        this.confirmTransaction = (Setting<Boolean>)this.register(new Setting("ConfirmTransaction", (T)true));
        this.timer = new Timer();
        this.packetList = new ConcurrentLinkedQueue<Packet<?>>();
    }
    
    public static double getIncremental(final double val, final double inc) {
        final double one = 1.0 / inc;
        return Math.round(val * one) / one;
    }
    
    public void onEnable() {
        this.timer.reset();
        this.blocking = true;
    }
    
    public void onUpdate() {
        if (this.timer.passedMs(this.waitTime.getValue())) {
            if (!this.packetList.isEmpty()) {
                this.blocking = false;
                int i = 0;
                final double totalPackets = getIncremental(Math.random() * 10.0, 1.0);
                for (final Packet packet : this.packetList) {
                    if (i < totalPackets) {
                        ++i;
                        PingSpoof.mc.getConnection().sendPacket(packet);
                        this.packetList.remove(packet);
                    }
                }
            }
            this.timer.reset();
            this.blocking = true;
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketSend(final PacketEvent.Send event) {
        if (this.blocking) {
            if (event.getPacket() instanceof CPacketKeepAlive && this.keepAlive.getValue()) {
                this.packetList.add((Packet<?>)event.getPacket());
                event.setCanceled(true);
            }
            else if (event.getPacket() instanceof CPacketConfirmTransaction && this.confirmTransaction.getValue()) {
                this.packetList.add((Packet<?>)event.getPacket());
                event.setCanceled(true);
            }
        }
    }
}

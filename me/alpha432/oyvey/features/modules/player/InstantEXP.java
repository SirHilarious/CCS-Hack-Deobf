//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.init.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.util.concurrent.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class InstantEXP extends Module
{
    private final Setting<Integer> delay;
    private final Setting<Integer> packets;
    private final Setting<Integer> offset;
    
    public InstantEXP() {
        super("InstantEXP", "Throws EXP Instantly", Module.Category.PLAYER, true, false, false);
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)0, (T)0, (T)250));
        this.packets = (Setting<Integer>)this.register(new Setting("Packets", (T)1, (T)1, (T)10));
        this.offset = (Setting<Integer>)this.register(new Setting("PacketOffset", (T)0, (T)0, (T)2));
    }
    
    @SubscribeEvent
    public void onPacketRecv(final PacketEvent.Receive event) {
        if (InstantEXP.mc.player != null && InstantEXP.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) {
            InstantEXP.mc.rightClickDelayTimer = 0;
        }
    }
    
    public void onUpdate() {
        if (InstantEXP.mc.gameSettings.keyBindUseItem.isKeyDown() && InstantEXP.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) {
            for (int i = 1 - this.offset.getValue(); i <= this.packets.getValue(); ++i) {
                this.sendPacket();
            }
        }
    }
    
    private void sendPacket() {
        final PacketThread packetThread = new PacketThread(this.delay.getValue());
        if (this.delay.getValue() == 0) {
            packetThread.run();
        }
        else {
            packetThread.start();
        }
    }
    
    public static class PacketThread extends Thread
    {
        private int delay;
        
        public PacketThread(final int delayIn) {
            this.delay = delayIn;
        }
        
        @Override
        public void run() {
            try {
                if (this.delay != 0) {
                    TimeUnit.MILLISECONDS.sleep(this.delay);
                }
                Util.mc.addScheduledTask(() -> Util.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND)));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

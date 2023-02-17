//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import java.util.concurrent.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import java.util.*;

public class AutoToxic extends Module
{
    private static AutoToxic INSTANCE;
    private ConcurrentHashMap<String, Integer> targetedPlayers;
    
    public AutoToxic() {
        super("AutoToxic", "Announces when people near you pop", Category.PLAYER, true, false, false);
        this.targetedPlayers = null;
        this.setInstance();
    }
    
    public static AutoToxic getINSTANCE() {
        if (AutoToxic.INSTANCE == null) {
            AutoToxic.INSTANCE = new AutoToxic();
        }
        return AutoToxic.INSTANCE;
    }
    
    private void setInstance() {
        AutoToxic.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
    }
    
    @Override
    public void onDisable() {
        this.targetedPlayers = null;
    }
    
    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        for (final Entity entity : AutoToxic.mc.world.getLoadedEntityList()) {
            final EntityPlayer player;
            if (entity instanceof EntityPlayer && (player = (EntityPlayer)entity).getHealth() <= 0.0f) {
                final String name2;
                if (!this.shouldAnnounce(name2 = player.getName())) {
                    continue;
                }
                this.doAnnounce(name2);
                break;
            }
        }
        this.targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.targetedPlayers.remove(name);
            }
            else {
                this.targetedPlayers.put(name, timeout - 1);
            }
        });
    }
    
    @SubscribeEvent
    public void onTotemPop(final TotemPopEvent event) {
        if (AutoToxic.mc.player == null) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        final EntityLivingBase entity;
        if ((entity = (EntityLivingBase)event.getEntity()) == null) {
            return;
        }
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        final EntityPlayer player = (EntityPlayer)entity;
        final String name = player.getName();
        if (this.shouldAnnounce(name)) {
            this.doAnnounce(name);
        }
    }
    
    private boolean shouldAnnounce(final String name) {
        return this.targetedPlayers.containsKey(name);
    }
    
    private void doAnnounce(final String name) {
        this.targetedPlayers.remove(name);
        AutoToxic.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("Ez pop " + name));
        int u = 0;
        for (int i = 0; i < 10; ++i) {
            ++u;
        }
    }
    
    public void addTargetedPlayer(final String name) {
        if (Objects.equals(name, AutoToxic.mc.player.getName())) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        this.targetedPlayers.put(name, 20);
    }
    
    static {
        AutoToxic.INSTANCE = new AutoToxic();
    }
}

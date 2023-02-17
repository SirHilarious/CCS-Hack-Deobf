//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;

public class ElytraFly2 extends Module
{
    private Setting<Boolean> fenable;
    private Setting<Float> speed;
    public static ElytraFly2 INSTANCE;
    
    public void setINSTANCE() {
        ElytraFly2.INSTANCE = this;
    }
    
    public static ElytraFly2 getINSTANCE() {
        if (ElytraFly2.INSTANCE == null) {
            ElytraFly2.INSTANCE = new ElytraFly2();
        }
        return ElytraFly2.INSTANCE;
    }
    
    public ElytraFly2() {
        super("ElytraFly2", "Uses Packets To Fly On An Elytra", Module.Category.MOVEMENT, true, false, false);
        this.fenable = (Setting<Boolean>)this.register(new Setting("AutoEnable", (T)false));
        this.speed = (Setting<Float>)this.register(new Setting("Speed", (T)6.2f, (T)1.0f, (T)10.0f));
        this.setINSTANCE();
    }
    
    public void onEnable() {
        if (this.fenable.getValue()) {
            ElytraFly2.mc.addScheduledTask(() -> {
                if (ElytraFly2.mc.player != null && !ElytraFly2.mc.player.isElytraFlying()) {
                    ElytraFly2.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFly2.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                }
            });
        }
    }
    
    public void onDisable() {
        this.disableFly();
        if (ElytraFly2.mc.player != null) {
            ElytraFly2.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFly2.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
    }
    
    public void onUpdate() {
        if (ElytraFly2.mc.player.isElytraFlying()) {
            this.enableFly();
        }
        ElytraFly2.mc.player.capabilities.setFlySpeed((float)this.speed.getValue());
    }
    
    private void enableFly() {
        if (ElytraFly2.mc.player == null || ElytraFly2.mc.player.capabilities == null) {
            return;
        }
        ElytraFly2.mc.player.capabilities.allowFlying = true;
        ElytraFly2.mc.player.capabilities.isFlying = true;
    }
    
    private void disableFly() {
        if (ElytraFly2.mc.player == null || ElytraFly2.mc.player.capabilities == null) {
            return;
        }
        final PlayerCapabilities gmCaps = new PlayerCapabilities();
        ElytraFly2.mc.playerController.getCurrentGameType().configurePlayerCapabilities(gmCaps);
        final PlayerCapabilities capabilities = ElytraFly2.mc.player.capabilities;
        capabilities.allowFlying = gmCaps.allowFlying;
        capabilities.isFlying = (gmCaps.allowFlying && capabilities.isFlying);
        capabilities.setFlySpeed(gmCaps.getFlySpeed());
    }
}

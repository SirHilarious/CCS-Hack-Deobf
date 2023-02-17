//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.*;
import net.minecraft.client.settings.*;
import net.minecraftforge.fml.client.registry.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.event.events.*;
import me.alpha432.oyvey.features.command.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.common.*;

public class PacketPhase extends Module
{
    KeyBinding left;
    KeyBinding right;
    KeyBinding down;
    KeyBinding up;
    long last;
    private boolean preventSetback;
    
    public PacketPhase() {
        super("PacketPhase", "Phase using packets", Module.Category.MOVEMENT, true, false, false);
        this.last = 0L;
        this.preventSetback = false;
        this.left = new KeyBinding("Left", 203, "combat");
        this.right = new KeyBinding("Right", 205, "combat");
        this.down = new KeyBinding("Down", 208, "combat");
        this.up = new KeyBinding("Up", 200, "combat");
        ClientRegistry.registerKeyBinding(this.left);
        ClientRegistry.registerKeyBinding(this.right);
        ClientRegistry.registerKeyBinding(this.down);
        ClientRegistry.registerKeyBinding(this.up);
    }
    
    @SubscribeEvent
    public void onPacketRecv(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook pak = (SPacketPlayerPosLook)event.getPacket();
            if (pak.yaw != PacketPhase.mc.player.rotationYaw || pak.pitch != PacketPhase.mc.player.rotationPitch) {
                PacketPhase.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Rotation(PacketPhase.mc.player.rotationYaw, PacketPhase.mc.player.rotationPitch, PacketPhase.mc.player.onGround));
                pak.yaw = PacketPhase.mc.player.rotationYaw;
                pak.pitch = PacketPhase.mc.player.rotationPitch;
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            return;
        }
        try {
            PacketPhase.mc.player.setVelocity(0.0, 0.0, 0.0);
            PacketPhase.mc.player.motionX = 0.0;
            PacketPhase.mc.player.motionY = 0.0;
            PacketPhase.mc.player.motionZ = 0.0;
            PacketPhase.mc.player.noClip = true;
            if (PacketPhase.mc.player.ticksExisted % 10 != 0) {
                return;
            }
            if (!this.preventSetback) {
                PacketPhase.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(PacketPhase.mc.player.posX + 0.5, PacketPhase.mc.player.posY, PacketPhase.mc.player.posZ - 0.5, false));
                this.preventSetback = false;
            }
            if (this.up.isKeyDown()) {
                final EntityPlayerSP player = PacketPhase.mc.player;
                --player.rotationPitch;
            }
            if (this.down.isKeyDown()) {
                final EntityPlayerSP player2 = PacketPhase.mc.player;
                ++player2.rotationPitch;
            }
            if (this.left.isKeyDown()) {
                final EntityPlayerSP player3 = PacketPhase.mc.player;
                --player3.rotationYaw;
            }
            if (this.right.isKeyDown()) {
                final EntityPlayerSP player4 = PacketPhase.mc.player;
                ++player4.rotationYaw;
            }
            double yaw = (PacketPhase.mc.player.rotationYaw + 90.0f) * -1.0f;
            double dO_numer = 0.0;
            double dO_denom = 0.0;
            if (PacketPhase.mc.gameSettings.keyBindLeft.isKeyDown()) {
                dO_numer -= 90.0;
                ++dO_denom;
            }
            if (PacketPhase.mc.gameSettings.keyBindRight.isKeyDown()) {
                dO_numer += 90.0;
                ++dO_denom;
            }
            if (PacketPhase.mc.gameSettings.keyBindBack.isKeyDown()) {
                dO_numer += 180.0;
                ++dO_denom;
            }
            if (PacketPhase.mc.gameSettings.keyBindForward.isKeyDown()) {
                ++dO_denom;
            }
            if (dO_denom > 0.0) {
                yaw += dO_numer / dO_denom % 361.0;
            }
            if (yaw < 0.0) {
                yaw = 360.0 - yaw;
            }
            if (yaw > 360.0) {
                yaw %= 361.0;
            }
            final double xDir = Math.cos(Math.toRadians(yaw));
            final double zDir = Math.sin(Math.toRadians(yaw));
            boolean keysDown = false;
            if (PacketPhase.mc.gameSettings.keyBindForward.isKeyDown() || PacketPhase.mc.gameSettings.keyBindLeft.isKeyDown() || PacketPhase.mc.gameSettings.keyBindRight.isKeyDown() || PacketPhase.mc.gameSettings.keyBindBack.isKeyDown()) {
                keysDown = true;
                PacketPhase.mc.player.motionX = xDir * 0.01;
                PacketPhase.mc.player.motionZ = zDir * 0.01;
            }
            else {
                PacketPhase.mc.player.motionX = 0.0;
                PacketPhase.mc.player.motionZ = 0.0;
            }
            Command.sendMessage("Motion: " + PacketPhase.mc.player.motionX + " | " + PacketPhase.mc.player.motionZ + " [] " + PacketPhase.mc.player.motionY);
            this.preventSetback = keysDown;
            PacketPhase.mc.player.motionY = 0.0;
            boolean yes;
            if (this.last + 50L >= System.currentTimeMillis()) {
                yes = false;
            }
            else {
                this.last = System.currentTimeMillis();
                yes = true;
            }
            if (yes) {
                PacketPhase.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(PacketPhase.mc.player.posX + PacketPhase.mc.player.motionX, PacketPhase.mc.player.posY + ((PacketPhase.mc.player.posY < 1.1) ? 0.1 : 0.0) + (PacketPhase.mc.gameSettings.keyBindJump.isKeyDown() ? 0.1 : 0.0) - (PacketPhase.mc.gameSettings.keyBindSneak.isKeyDown() ? 0.1 : 0.0), PacketPhase.mc.player.posZ + PacketPhase.mc.player.motionZ, false));
                PacketPhase.mc.player.setLocationAndAngles(PacketPhase.mc.player.posX + PacketPhase.mc.player.motionX, PacketPhase.mc.player.posY, PacketPhase.mc.player.posZ + PacketPhase.mc.player.motionZ, PacketPhase.mc.player.rotationYaw, PacketPhase.mc.player.rotationPitch);
                PacketPhase.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(PacketPhase.mc.player.posX + PacketPhase.mc.player.motionX, PacketPhase.mc.player.posY - 42069.0, PacketPhase.mc.player.posZ + PacketPhase.mc.player.motionZ, true));
            }
        }
        catch (Exception e) {
            Command.sendMessage("PacketFly encountered an error, Disabling...");
            e.printStackTrace();
            this.disable();
        }
    }
    
    public void onEnable() {
        this.preventSetback = false;
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public void onDisable() {
        if (PacketPhase.mc.player != null) {
            PacketPhase.mc.player.noClip = false;
        }
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
}

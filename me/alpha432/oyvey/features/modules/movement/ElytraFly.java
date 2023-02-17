//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.client.entity.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.network.play.client.*;

public class ElytraFly extends Module
{
    private Setting<Float> speed;
    private Setting<Float> UpSpeed;
    private Setting<Float> DownSpeed;
    private Setting<Float> GlideSpeed;
    private Setting<Boolean> Accelerate;
    private Setting<Integer> vAccelerationTimer;
    private Setting<Float> RotationPitch;
    private Setting<Boolean> CancelInWater;
    private Setting<Boolean> InstantFly;
    private Setting<Boolean> EquipElytra;
    private Setting<Boolean> PitchSpoof;
    private Setting<Boolean> infDura;
    private Timer PacketTimer;
    private Timer AccelerationTimer;
    private Timer AccelerationResetTimer;
    private Timer InstantFlyTimer;
    private int ElytraSlot;
    
    public ElytraFly() {
        super("ElytraFly", "Modifys the flight of elytras", Module.Category.MOVEMENT, true, false, false);
        this.speed = (Setting<Float>)this.register(new Setting("Speed", (T)1.82f, (T)0.0f, (T)10.0f));
        this.UpSpeed = (Setting<Float>)this.register(new Setting("UpSpeed", (T)2.0f, (T)2.0f, (T)10.0f));
        this.DownSpeed = (Setting<Float>)this.register(new Setting("DownSpeed", (T)1.82f, (T)0.0f, (T)10.0f));
        this.GlideSpeed = (Setting<Float>)this.register(new Setting("GlideSpeed", (T)0.0f, (T)1.0f, (T)10.0f));
        this.Accelerate = (Setting<Boolean>)this.register(new Setting("Accelerate", (T)true));
        this.vAccelerationTimer = (Setting<Integer>)this.register(new Setting("Timer", (T)1000, (T)0, (T)10000));
        this.RotationPitch = (Setting<Float>)this.register(new Setting("RotationPitch", (T)0.0f, (T)(-90.0f), (T)90.0f));
        this.CancelInWater = (Setting<Boolean>)this.register(new Setting("CancelInWater", (T)true));
        this.InstantFly = (Setting<Boolean>)this.register(new Setting("InstantFly", (T)true));
        this.EquipElytra = (Setting<Boolean>)this.register(new Setting("EquipElytra", (T)false));
        this.PitchSpoof = (Setting<Boolean>)this.register(new Setting("PitchSpoof", (T)true));
        this.infDura = (Setting<Boolean>)this.register(new Setting("InfiniteDurability", (T)true));
        this.PacketTimer = new Timer();
        this.AccelerationTimer = new Timer();
        this.AccelerationResetTimer = new Timer();
        this.InstantFlyTimer = new Timer();
        this.ElytraSlot = -1;
    }
    
    public void onEnable() {
        super.onEnable();
        this.ElytraSlot = -1;
        if (this.EquipElytra.getValue() && ElytraFly.mc.player != null && ElytraFly.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
            for (int l_I = 0; l_I < 44; ++l_I) {
                final ItemStack l_Stack = ElytraFly.mc.player.inventory.getStackInSlot(l_I);
                if (!l_Stack.isEmpty() && l_Stack.getItem() == Items.ELYTRA) {
                    final ItemElytra l_Elytra = (ItemElytra)l_Stack.getItem();
                    this.ElytraSlot = l_I;
                    break;
                }
            }
            if (this.ElytraSlot != -1) {
                final boolean l_HasArmorAtChest = ElytraFly.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.AIR;
                ElytraFly.mc.playerController.windowClick(ElytraFly.mc.player.inventoryContainer.windowId, this.ElytraSlot, 0, ClickType.PICKUP, (EntityPlayer)ElytraFly.mc.player);
                ElytraFly.mc.playerController.windowClick(ElytraFly.mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, (EntityPlayer)ElytraFly.mc.player);
                if (l_HasArmorAtChest) {
                    ElytraFly.mc.playerController.windowClick(ElytraFly.mc.player.inventoryContainer.windowId, this.ElytraSlot, 0, ClickType.PICKUP, (EntityPlayer)ElytraFly.mc.player);
                }
            }
        }
    }
    
    public void onDisable() {
        super.onDisable();
        if (ElytraFly.mc.player == null) {
            return;
        }
        if (this.ElytraSlot != -1) {
            final boolean l_HasItem = !ElytraFly.mc.player.inventory.getStackInSlot(this.ElytraSlot).isEmpty() || ElytraFly.mc.player.inventory.getStackInSlot(this.ElytraSlot).getItem() != Items.AIR;
            ElytraFly.mc.playerController.windowClick(ElytraFly.mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, (EntityPlayer)ElytraFly.mc.player);
            ElytraFly.mc.playerController.windowClick(ElytraFly.mc.player.inventoryContainer.windowId, this.ElytraSlot, 0, ClickType.PICKUP, (EntityPlayer)ElytraFly.mc.player);
            if (l_HasItem) {
                ElytraFly.mc.playerController.windowClick(ElytraFly.mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, (EntityPlayer)ElytraFly.mc.player);
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PlayerTravelEvent event) {
        if (ElytraFly.mc.player == null) {
            return;
        }
        if (ElytraFly.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
            return;
        }
        if (!ElytraFly.mc.player.isElytraFlying()) {
            if (!ElytraFly.mc.player.onGround && this.InstantFly.getValue() && !this.infDura.getValue()) {
                if (!this.InstantFlyTimer.passedMs(1000L)) {
                    return;
                }
                this.InstantFlyTimer.reset();
                ElytraFly.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
            return;
        }
        this.HandleNormalModeElytra(event);
    }
    
    public void HandleNormalModeElytra(final PlayerTravelEvent p_Travel) {
        final double l_YHeight = ElytraFly.mc.player.posY;
        final boolean l_IsMoveKeyDown = ElytraFly.mc.player.movementInput.moveForward > 0.0f || ElytraFly.mc.player.movementInput.moveStrafe > 0.0f;
        final boolean l_CancelInWater = !ElytraFly.mc.player.isInWater() && !ElytraFly.mc.player.isInLava() && this.CancelInWater.getValue();
        if (ElytraFly.mc.player.movementInput.jump) {
            p_Travel.setCanceled(true);
            this.Accelerate();
            return;
        }
        if (!l_IsMoveKeyDown) {
            this.AccelerationTimer.resetTimeSkipTo(-this.vAccelerationTimer.getValue());
        }
        else if (ElytraFly.mc.player.rotationPitch <= this.RotationPitch.getValue() && l_CancelInWater) {
            if (this.Accelerate.getValue() && this.AccelerationTimer.passedMs(this.vAccelerationTimer.getValue())) {
                this.Accelerate();
            }
            return;
        }
        p_Travel.setCanceled(true);
        this.Accelerate();
    }
    
    public void Accelerate() {
        if (this.AccelerationResetTimer.passedMs(this.vAccelerationTimer.getValue())) {
            this.AccelerationResetTimer.reset();
            this.AccelerationTimer.reset();
        }
        final float l_Speed = this.speed.getValue();
        final double[] dir = MathUtil.directionSpeed(l_Speed);
        ElytraFly.mc.player.motionY = -(this.GlideSpeed.getValue() / 10000.0f);
        if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
            ElytraFly.mc.player.motionX = dir[0];
            ElytraFly.mc.player.motionZ = dir[1];
        }
        else {
            ElytraFly.mc.player.motionX = 0.0;
            ElytraFly.mc.player.motionZ = 0.0;
        }
        if (ElytraFly.mc.player.movementInput.sneak) {
            ElytraFly.mc.player.motionY = -this.DownSpeed.getValue();
        }
        ElytraFly.mc.player.prevLimbSwingAmount = 0.0f;
        ElytraFly.mc.player.limbSwingAmount = 0.0f;
        ElytraFly.mc.player.limbSwing = 0.0f;
    }
    
    private void HandleControlMode(final PlayerTravelEvent p_Event) {
        final double[] dir = MathUtil.directionSpeed(this.speed.getValue());
        if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
            ElytraFly.mc.player.motionX = dir[0];
            ElytraFly.mc.player.motionZ = dir[1];
            final EntityPlayerSP player = ElytraFly.mc.player;
            player.motionX -= ElytraFly.mc.player.motionX * (Math.abs(ElytraFly.mc.player.rotationPitch) + 90.0f) / 90.0 - ElytraFly.mc.player.motionX;
            final EntityPlayerSP player2 = ElytraFly.mc.player;
            player2.motionZ -= ElytraFly.mc.player.motionZ * (Math.abs(ElytraFly.mc.player.rotationPitch) + 90.0f) / 90.0 - ElytraFly.mc.player.motionZ;
        }
        else {
            ElytraFly.mc.player.motionX = 0.0;
            ElytraFly.mc.player.motionZ = 0.0;
        }
        ElytraFly.mc.player.motionY = -MathUtil.degToRad(ElytraFly.mc.player.rotationPitch) * ElytraFly.mc.player.movementInput.moveForward;
        ElytraFly.mc.player.prevLimbSwingAmount = 0.0f;
        ElytraFly.mc.player.limbSwingAmount = 0.0f;
        ElytraFly.mc.player.limbSwing = 0.0f;
        p_Event.setCanceled(true);
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && this.PitchSpoof.getValue()) {
            if (!ElytraFly.mc.player.isElytraFlying()) {
                return;
            }
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation && this.PitchSpoof.getValue()) {
                final CPacketPlayer.PositionRotation rotation = (CPacketPlayer.PositionRotation)event.getPacket();
                ElytraFly.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(rotation.x, rotation.y, rotation.z, rotation.onGround));
                event.setCanceled(true);
            }
            else if (event.getPacket() instanceof CPacketPlayer.Rotation && this.PitchSpoof.getValue()) {
                event.setCanceled(true);
            }
        }
    }
}

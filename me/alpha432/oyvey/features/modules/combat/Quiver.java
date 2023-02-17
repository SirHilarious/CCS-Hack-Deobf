//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.util.*;
import net.minecraft.potion.*;
import net.minecraft.item.*;
import java.util.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;
import net.minecraft.network.play.client.*;

public class Quiver extends Module
{
    private final Setting<Float> bowDelay;
    private final Setting<Float> shootDelay;
    private final Setting<Boolean> strength;
    private final Setting<Boolean> speed;
    private int lastShot;
    
    public Quiver() {
        super("Quiver", "Rotates and shoots yourself with good potion effects", Category.COMBAT, true, false, false);
        this.bowDelay = (Setting<Float>)this.register(new Setting("BowDelay", (T)4.0, (T)1.0, (T)20.0));
        this.shootDelay = (Setting<Float>)this.register(new Setting("ShootDelay", (T)10.0, (T)2.0, (T)40.0));
        this.strength = (Setting<Boolean>)this.register(new Setting("Strength", (T)true));
        this.speed = (Setting<Boolean>)this.register(new Setting("Speed", (T)true));
    }
    
    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (this.lastShot < 50) {
            ++this.lastShot;
        }
        final boolean hasSpeed = Quiver.mc.player.getActivePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById(1))) != null;
        final boolean hasStrength = Quiver.mc.player.getActivePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById(5))) != null;
        final int bowSlot = PlayerUtil.getHotbarSlot((Item)Items.BOW);
        final List<Integer> tippedArrowSlots = PlayerUtil.getInventorySlots(Items.TIPPED_ARROW);
        int speedArrowSlot = -1;
        boolean foundSpeedArrowSlot = false;
        int strengthArrowSlot = -1;
        boolean foundStrengthArrowSlot = false;
        for (final Integer slot : tippedArrowSlots) {
            if (!foundSpeedArrowSlot && Objects.requireNonNull(PotionUtils.getPotionFromItem(Quiver.mc.player.inventory.getStackInSlot((int)slot)).getRegistryName()).getPath().contains("swiftness")) {
                speedArrowSlot = slot;
                foundSpeedArrowSlot = true;
            }
            if (!foundStrengthArrowSlot && Objects.requireNonNull(PotionUtils.getPotionFromItem(Quiver.mc.player.inventory.getStackInSlot((int)slot)).getRegistryName()).getPath().contains("strength")) {
                strengthArrowSlot = slot;
                foundStrengthArrowSlot = true;
            }
        }
        if (this.speed.getValue() && !hasSpeed && speedArrowSlot != -1 && (speedArrowSlot < strengthArrowSlot || strengthArrowSlot == -1) && bowSlot != -1 && Quiver.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && Quiver.mc.player.isHandActive() && Quiver.mc.player.getItemInUseMaxCount() >= this.bowDelay.getValue() && this.shootDelay.getValue() < this.lastShot) {
            this.doShooting();
        }
        if (this.speed.getValue() && !hasSpeed && strengthArrowSlot != -1 && speedArrowSlot != -1 && speedArrowSlot > strengthArrowSlot && bowSlot != -1 && Quiver.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && Quiver.mc.player.isHandActive() && Quiver.mc.player.getItemInUseMaxCount() >= 1) {
            PlayerUtil.swapSlots(speedArrowSlot, strengthArrowSlot);
        }
        if (hasSpeed || !this.speed.getValue() || speedArrowSlot == -1) {
            if (this.strength.getValue() && !hasStrength && strengthArrowSlot != -1 && (strengthArrowSlot < speedArrowSlot || speedArrowSlot == -1) && bowSlot != -1 && Quiver.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && Quiver.mc.player.isHandActive() && Quiver.mc.player.getItemInUseMaxCount() >= this.bowDelay.getValue() && this.shootDelay.getValue() < this.lastShot) {
                this.doShooting();
            }
            if (this.strength.getValue() && !hasStrength && strengthArrowSlot != -1 && speedArrowSlot != -1 && strengthArrowSlot > speedArrowSlot && bowSlot != -1 && Quiver.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && Quiver.mc.player.isHandActive() && Quiver.mc.player.getItemInUseMaxCount() >= 1) {
                PlayerUtil.swapSlots(speedArrowSlot, strengthArrowSlot);
            }
        }
    }
    
    private void doShooting() {
        Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(Quiver.mc.player.rotationYaw, -90.0f, Quiver.mc.player.onGround));
        Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Quiver.mc.player.getHorizontalFacing()));
        Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(Quiver.mc.player.getActiveHand()));
        Quiver.mc.player.stopActiveHand();
        this.lastShot = 0;
    }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;
import net.minecraft.network.play.server.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.event.events.*;
import java.awt.*;
import me.alpha432.oyvey.features.command.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.*;
import net.minecraft.network.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;

public class AutoHeadCrystal extends Module
{
    private Setting<Boolean> rotate;
    private Setting<Boolean> rotateA;
    private Setting<Integer> range;
    private Setting<Integer> ticksExisted;
    private Setting<Integer> attackDelay;
    private BlockPos targetBlock;
    private Timer timer;
    private Timer attackTimer;
    private int lastHotbarSlot;
    private int stage;
    private boolean doAttack;
    private EntityEnderCrystal crystal;
    private EntityPlayer target;
    private Vec3d headOffset;
    private BlockPos headBlockPos;
    
    public AutoHeadCrystal() {
        super("AutoHeadCrystal", "Automatically head crystals using instantmine", Category.COMBAT, true, false, false);
        this.rotate = (Setting<Boolean>)this.register(new Setting("RotatePlace", (T)true));
        this.rotateA = (Setting<Boolean>)this.register(new Setting("RotateAttack", (T)false));
        this.range = (Setting<Integer>)this.register(new Setting("Target Range", (T)4, (T)1, (T)10));
        this.ticksExisted = (Setting<Integer>)this.register(new Setting("Ticks Existed", (T)2, (T)0, (T)5));
        this.attackDelay = (Setting<Integer>)this.register(new Setting("Attack Delay", (T)1000, (T)0, (T)2000));
        this.timer = new Timer();
        this.attackTimer = new Timer();
        this.lastHotbarSlot = -1;
        this.stage = 0;
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            try {
                final SPacketSoundEffect sPacketSoundEffect = (SPacketSoundEffect)event.getPacket();
                if (sPacketSoundEffect.getCategory() == SoundCategory.BLOCKS && sPacketSoundEffect.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    for (final Entity e : AutoHeadCrystal.mc.world.loadedEntityList) {
                        if (e instanceof EntityEnderCrystal && e.getDistance(sPacketSoundEffect.getX(), sPacketSoundEffect.getY(), sPacketSoundEffect.getZ()) <= this.range.getValue()) {
                            Objects.requireNonNull(AutoHeadCrystal.mc.world.getEntityByID(e.getEntityId())).setDead();
                            AutoHeadCrystal.mc.world.removeEntityFromWorld(e.entityId);
                            this.doAttack = false;
                        }
                    }
                }
            }
            catch (Exception ex) {}
        }
    }
    
    @Override
    public void onToggle() {
        this.stage = 0;
        this.doAttack = false;
        this.attackTimer.reset();
        this.lastHotbarSlot = AutoHeadCrystal.mc.player.inventory.currentItem;
        this.target = null;
        this.headOffset = null;
        this.headBlockPos = null;
        this.targetBlock = null;
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.targetBlock != null) {
            RenderUtil.drawBoxESP(this.targetBlock, new Color(208, 0, 255, 255), 1.2f, true, true, 50);
        }
    }
    
    @Override
    public void onUpdate() {
        if (PlayerUtil.getHotbarSlot(Blocks.OBSIDIAN) == -1) {
            Command.sendMessage("There is no Obsidian in your hotbar, Cannot continue");
            return;
        }
        if (PlayerUtil.getHotbarSlot(Items.DIAMOND_PICKAXE) == -1) {
            Command.sendMessage("There is no Pickaxe in your hotbar, Cannot continue");
            return;
        }
        if (PlayerUtil.getHotbarSlot(Items.END_CRYSTAL) == -1) {
            Command.sendMessage("There is no EndCrystals in your hotbar, Cannot continue");
            return;
        }
        if (this.targetBlock == null || this.target == null || this.target.isDead || this.target.getHealth() <= 0.0f) {
            this.doAttack = false;
            this.headOffset = null;
            this.headBlockPos = null;
            this.stage = 0;
        }
        if (this.doAttack) {
            if (this.attackTimer.passedMs(this.attackDelay.getValue())) {
                if (this.crystal != null) {
                    if (this.crystal.ticksExisted >= this.ticksExisted.getValue()) {
                        if (this.rotateA.getValue()) {
                            RotationUtil.faceEntity((Entity)this.crystal);
                        }
                        EntityUtil.attackEntity((Entity)this.crystal, false, true);
                        this.attackTimer.reset();
                    }
                }
                else {
                    this.doAttack = false;
                }
            }
            return;
        }
        if (this.stage == 0) {
            for (final EntityPlayer player : AutoHeadCrystal.mc.world.playerEntities) {
                if (player != null && player != AutoHeadCrystal.mc.player && player.getDistance((Entity)AutoHeadCrystal.mc.player) <= this.range.getValue() && !OyVey.friendManager.isFriend(player.getName())) {
                    this.target = player;
                }
            }
            if (this.target != null) {
                this.headOffset = this.target.getPositionVector().add(0.0, 2.0, 0.0);
                this.headBlockPos = new BlockPos(this.headOffset);
                if (AutoHeadCrystal.mc.world.getBlockState(this.headBlockPos).getBlock() != Blocks.OBSIDIAN) {
                    AutoHeadCrystal.mc.player.inventory.currentItem = PlayerUtil.getHotbarSlot(Blocks.OBSIDIAN);
                    AutoHeadCrystal.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(PlayerUtil.getHotbarSlot(Blocks.OBSIDIAN)));
                    BlockUtil.placeBlock(this.headBlockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, AutoHeadCrystal.mc.player.isSneaking());
                }
                if (this.targetBlock == null || (this.targetBlock.x != this.headBlockPos.x && this.targetBlock.y != this.headBlockPos.y && this.targetBlock.z != this.headBlockPos.z)) {
                    this.targetBlock = this.headBlockPos;
                    AutoHeadCrystal.mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.targetBlock, EnumFacing.DOWN));
                    this.timer.reset();
                }
                ++this.stage;
            }
        }
        else {
            if (this.stage == 1) {
                if (this.timer.passedMs((int)(2000.0f * OyVey.serverManager.getTpsFactor()))) {
                    AutoHeadCrystal.mc.player.inventory.currentItem = PlayerUtil.getHotbarSlot(Items.END_CRYSTAL);
                    AutoHeadCrystal.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(PlayerUtil.getHotbarSlot(Items.END_CRYSTAL)));
                    if (this.rotate.getValue()) {
                        RotationUtil.faceVector(this.headOffset, true);
                    }
                    AutoHeadCrystal.mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.targetBlock, EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    this.stage = 2;
                }
                return;
            }
            if (this.stage == 2) {
                AutoHeadCrystal.mc.player.inventory.currentItem = PlayerUtil.getHotbarSlot(Items.DIAMOND_PICKAXE);
                AutoHeadCrystal.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(PlayerUtil.getHotbarSlot(Items.DIAMOND_PICKAXE)));
                if (this.rotate.getValue()) {
                    RotationUtil.faceVector(this.headOffset, true);
                }
                AutoHeadCrystal.mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.targetBlock, EnumFacing.DOWN));
                this.stage = 3;
                return;
            }
            if (this.stage == 3) {
                AutoHeadCrystal.mc.world.loadedEntityList.forEach(entity -> {
                    if (entity != null && entity instanceof EntityEnderCrystal && !((Entity)entity).isDead && ((Entity)entity).getDistance((Entity)AutoHeadCrystal.mc.player) < this.range.getValue()) {
                        this.crystal = entity;
                    }
                    return;
                });
                if (this.crystal != null) {
                    this.doAttack = true;
                }
                this.stage = 0;
            }
        }
    }
}

//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import java.util.*;
import me.alpha432.oyvey.features.command.*;
import net.minecraft.network.*;
import net.minecraft.init.*;
import net.minecraft.block.state.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.block.*;
import net.minecraft.item.*;

public class SelfFill extends Module
{
    private BlockPos playerPos;
    public Setting<Boolean> Echest;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> oldfag;
    private final double[] firstPositions;
    public static SelfFill INSTANCE;
    private boolean toggledCa;
    
    public SelfFill() {
        super("Burrow", "Sets you into a block", Category.COMBAT, true, false, true);
        this.Echest = (Setting<Boolean>)this.register(new Setting("PreferEchest", (T)false));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true));
        this.oldfag = (Setting<Boolean>)this.register(new Setting("OldFag", (T)false));
        this.firstPositions = new double[] { 0.42, 0.75, 1.0, 1.16 };
        SelfFill.INSTANCE = this;
    }
    
    public static SelfFill getInstance() {
        if (SelfFill.INSTANCE == null) {
            SelfFill.INSTANCE = new SelfFill();
        }
        return SelfFill.INSTANCE;
    }
    
    public static boolean isInterceptedByOther(final BlockPos pos) {
        for (final Entity entity : SelfFill.mc.world.loadedEntityList) {
            if (entity.equals((Object)SelfFill.mc.player)) {
                continue;
            }
            if (entity instanceof EntityItem) {
                continue;
            }
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onEnable() {
        if (SelfFill.mc.player == null || SelfFill.mc.world == null) {
            this.disable();
            return;
        }
        if (this.getBlock() == null) {
            Command.sendMessage("No Blocks In Hotbar");
            this.disable();
            return;
        }
        this.playerPos = new BlockPos(SelfFill.mc.player.posX, SelfFill.mc.player.posY, SelfFill.mc.player.posZ);
        if (SelfFill.mc.world.getBlockState(this.playerPos).getBlock().equals(this.getBlock())) {
            this.disable();
            return;
        }
        if (isInterceptedByOther(this.playerPos)) {
            this.disable();
            return;
        }
        this.toggledCa = false;
        if (AutoCrystal.getInstance().isOn()) {
            this.toggledCa = true;
            AutoCrystal.getInstance().disable();
        }
        SelfFill.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)SelfFill.mc.player, CPacketEntityAction.Action.START_SNEAKING));
        if (this.oldfag.getValue()) {
            for (final double position : this.firstPositions) {
                SelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(SelfFill.mc.player.posX + position, SelfFill.mc.player.posY + position, SelfFill.mc.player.posZ + position, SelfFill.mc.player.onGround));
            }
        }
        else {
            for (final double position : this.firstPositions) {
                SelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(SelfFill.mc.player.posX, SelfFill.mc.player.posY + position, SelfFill.mc.player.posZ, SelfFill.mc.player.onGround));
            }
        }
        this.placeBlock(this.playerPos, getHotbarSlot(this.getBlock()));
        this.doBurrow();
    }
    
    @Override
    public void onUpdate() {
    }
    
    public void doBurrow() {
        int y;
        int offset;
        for (offset = (y = 2); y < SelfFill.mc.world.getHeight() - SelfFill.mc.player.posY; ++y) {
            final IBlockState scanState1 = SelfFill.mc.world.getBlockState(new BlockPos(SelfFill.mc.player.posX, SelfFill.mc.player.posY, SelfFill.mc.player.posZ).up(y));
            if (scanState1.getBlock() == Blocks.AIR) {
                final IBlockState scanState2 = SelfFill.mc.world.getBlockState(new BlockPos(SelfFill.mc.player.posX, SelfFill.mc.player.posY, SelfFill.mc.player.posZ).up(y + 1));
                if (scanState2.getBlock() == Blocks.AIR) {
                    offset = y;
                    break;
                }
            }
        }
        if (this.oldfag.getValue()) {
            SelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(SelfFill.mc.player.posX + 1.16, SelfFill.mc.player.posY + 1.16, SelfFill.mc.player.posZ, false));
        }
        else {
            SelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(SelfFill.mc.player.posX, SelfFill.mc.player.posY + offset, SelfFill.mc.player.posZ, false));
        }
        this.disable();
    }
    
    @Override
    public void onDisable() {
        if (this.toggledCa) {
            AutoCrystal.getInstance().enable();
        }
        SelfFill.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)SelfFill.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
    }
    
    public void placeBlock(final BlockPos pos) {
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            if (!SelfFill.mc.world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR)) {
                final Vec3d vec = new Vec3d(pos.getX() + 0.5 + enumFacing.getXOffset() * 0.5, pos.getY() + 0.5 + enumFacing.getYOffset() * 0.5, pos.getZ() + 0.5 + enumFacing.getZOffset() * 0.5);
                final float[] old = { SelfFill.mc.player.rotationYaw, SelfFill.mc.player.rotationPitch };
                if (this.rotate.getValue()) {
                    SelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation((float)Math.toDegrees(Math.atan2(vec.z - SelfFill.mc.player.posZ, vec.x - SelfFill.mc.player.posX)) - 90.0f, (float)(-Math.toDegrees(Math.atan2(vec.y - (SelfFill.mc.player.posY + SelfFill.mc.player.getEyeHeight()), Math.sqrt((vec.x - SelfFill.mc.player.posX) * (vec.x - SelfFill.mc.player.posX) + (vec.z - SelfFill.mc.player.posZ) * (vec.z - SelfFill.mc.player.posZ))))), false));
                }
                final Vec3d vector = new Vec3d((Vec3i)pos);
                final float f = (float)(vector.x - pos.getX());
                final float f2 = (float)(vector.y - pos.getY());
                final float f3 = (float)(vector.z - pos.getZ());
                SelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos.offset(enumFacing), enumFacing.getOpposite(), EnumHand.MAIN_HAND, f, f2, f3));
                SelfFill.mc.player.swingArm(EnumHand.MAIN_HAND);
                if (this.rotate.getValue()) {
                    SelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(old[0], old[1], false));
                }
                return;
            }
        }
    }
    
    public void placeBlock(final BlockPos pos, final int slot) {
        if (slot == -1) {
            return;
        }
        final int prev = SelfFill.mc.player.inventory.currentItem;
        SelfFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
        this.placeBlock(pos);
        SelfFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(prev));
    }
    
    public static int getHotbarSlot(final Block block) {
        for (int i = 0; i < 9; ++i) {
            final Item item = SelfFill.mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock)item).getBlock().equals(block)) {
                return i;
            }
        }
        return -1;
    }
    
    private Block getBlock() {
        if ((!this.Echest.getValue() && getHotbarSlot(Blocks.OBSIDIAN) != -1) || (getHotbarSlot(Blocks.OBSIDIAN) != -1 && getHotbarSlot(Blocks.ENDER_CHEST) == -1)) {
            return Blocks.OBSIDIAN;
        }
        if (getHotbarSlot(Blocks.ENDER_CHEST) != -1) {
            return Blocks.ENDER_CHEST;
        }
        return null;
    }
}

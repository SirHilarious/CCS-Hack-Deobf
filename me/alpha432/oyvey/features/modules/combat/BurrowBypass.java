//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import java.util.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.world.*;
import net.minecraft.network.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import me.alpha432.oyvey.features.command.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.item.*;

public class BurrowBypass extends Module
{
    private List<Block> invalid;
    private BlockData blockData;
    private final double[] offsets;
    private BlockPos playerPos;
    
    public BurrowBypass() {
        super("SlabBurrow", "briish man module", Category.COMBAT, true, false, false);
        this.offsets = new double[] { 0.419999986886978, 0.753199980521202, 1.001335979112148, 1.166109260938214 };
        this.invalid = Arrays.asList(Blocks.AIR, (Block)Blocks.WATER, (Block)Blocks.FIRE, (Block)Blocks.FLOWING_WATER, (Block)Blocks.LAVA, (Block)Blocks.FLOWING_LAVA);
    }
    
    private float[] aimAtLocation(final double x, final double y, final double z, final EnumFacing facing) {
        final EntitySnowball temp = new EntitySnowball((World)BurrowBypass.mc.world);
        temp.posX = x + 0.5;
        temp.posY = y - 0.6;
        temp.posZ = z + 0.5;
        return this.aimAtLocation(temp.posX, temp.posY, temp.posZ);
    }
    
    private float[] aimAtLocation(final double positionX, final double positionY, final double positionZ) {
        final double x = positionX - BurrowBypass.mc.player.posX;
        final double y = positionY - BurrowBypass.mc.player.posY;
        final double z = positionZ - BurrowBypass.mc.player.posZ;
        final double distance = MathHelper.sqrt(x * x + z * z);
        return new float[] { (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f, (float)(-(Math.atan2(y, distance) * 180.0 / 3.141592653589793)) };
    }
    
    private BlockData getBlockData(final BlockPos pos, final List list) {
        return list.contains(BurrowBypass.mc.world.getBlockState(pos.add(0, -1, 0)).getBlock()) ? (list.contains(BurrowBypass.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock()) ? (list.contains(BurrowBypass.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock()) ? (list.contains(BurrowBypass.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock()) ? (list.contains(BurrowBypass.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock()) ? null : new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH)) : new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH)) : new BlockData(pos.add(1, 0, 0), EnumFacing.WEST)) : new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST)) : new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.setEnabled(false);
            return;
        }
        this.playerPos = new BlockPos(BurrowBypass.mc.player.posX, BurrowBypass.mc.player.posY + 0.9, BurrowBypass.mc.player.posZ);
        for (final double position : this.offsets) {
            BurrowBypass.mc.player.setPosition(BurrowBypass.mc.player.posX, BurrowBypass.mc.player.posY + position, BurrowBypass.mc.player.posZ);
        }
        BlockPos blockBelow1 = new BlockPos(BurrowBypass.mc.player.posX, BurrowBypass.mc.player.posY, BurrowBypass.mc.player.posZ);
        if (BurrowBypass.mc.world.getBlockState(new BlockPos(BurrowBypass.mc.player.posX + 1.0, BurrowBypass.mc.player.posY, BurrowBypass.mc.player.posZ)).getBlock() != Blocks.AIR) {
            blockBelow1 = new BlockPos(BurrowBypass.mc.player.posX + 1.0, BurrowBypass.mc.player.posY, BurrowBypass.mc.player.posZ);
        }
        else if (BurrowBypass.mc.world.getBlockState(new BlockPos(BurrowBypass.mc.player.posX - 1.0, BurrowBypass.mc.player.posY, BurrowBypass.mc.player.posZ)).getBlock() != Blocks.AIR) {
            blockBelow1 = new BlockPos(BurrowBypass.mc.player.posX - 1.0, BurrowBypass.mc.player.posY, BurrowBypass.mc.player.posZ);
        }
        else if (BurrowBypass.mc.world.getBlockState(new BlockPos(BurrowBypass.mc.player.posX, BurrowBypass.mc.player.posY, BurrowBypass.mc.player.posZ + 1.0)).getBlock() != Blocks.AIR) {
            blockBelow1 = new BlockPos(BurrowBypass.mc.player.posX, BurrowBypass.mc.player.posY, BurrowBypass.mc.player.posZ + 1.0);
        }
        else if (BurrowBypass.mc.world.getBlockState(new BlockPos(BurrowBypass.mc.player.posX, BurrowBypass.mc.player.posY, BurrowBypass.mc.player.posZ - 1.0)).getBlock() != Blocks.AIR) {
            blockBelow1 = new BlockPos(BurrowBypass.mc.player.posX, BurrowBypass.mc.player.posY, BurrowBypass.mc.player.posZ - 1.0);
        }
        this.blockData = this.getBlockData(blockBelow1, this.invalid);
        final float yaw = this.aimAtLocation(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face)[0];
        final float pitch = this.aimAtLocation(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ(), this.blockData.face)[1];
        BurrowBypass.mc.player.rotationYaw = yaw;
        BurrowBypass.mc.player.rotationPitch = pitch;
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            this.setEnabled(false);
            return;
        }
        this.placeBlock(this.playerPos, getHotbarSlot(this.getBlock()));
        BurrowBypass.mc.player.setPosition(BurrowBypass.mc.player.posX, BurrowBypass.mc.player.posY + 3.0, BurrowBypass.mc.player.posZ);
        this.disable();
    }
    
    public void placeBlock(final BlockPos pos) {
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            if (!BurrowBypass.mc.world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR)) {
                final Vec3d vec = new Vec3d(pos.getX() + 0.5 + enumFacing.getXOffset() * 0.5, pos.getY() + 0.5 + enumFacing.getYOffset() * 0.5, pos.getZ() + 0.5 + enumFacing.getZOffset() * 0.5);
                final float[] old = { BurrowBypass.mc.player.rotationYaw, BurrowBypass.mc.player.rotationPitch };
                BurrowBypass.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation((float)Math.toDegrees(Math.atan2(vec.z - BurrowBypass.mc.player.posZ, vec.x - BurrowBypass.mc.player.posX)) - 90.0f, (float)(-Math.toDegrees(Math.atan2(vec.y - (BurrowBypass.mc.player.posY + BurrowBypass.mc.player.getEyeHeight()), Math.sqrt((vec.x - BurrowBypass.mc.player.posX) * (vec.x - BurrowBypass.mc.player.posX) + (vec.z - BurrowBypass.mc.player.posZ) * (vec.z - BurrowBypass.mc.player.posZ))))), false));
                BurrowBypass.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BurrowBypass.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                final Vec3d vector = new Vec3d((Vec3i)pos);
                final float f = (float)(vector.x - pos.getX());
                final float f2 = (float)(vector.y - pos.getY());
                final float f3 = (float)(vector.z - pos.getZ());
                Command.sendMessage("Placing " + pos.offset(enumFacing) + " Required " + pos);
                BurrowBypass.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos.offset(enumFacing), this.blockData.face, EnumHand.MAIN_HAND, f, f2, f3));
                BurrowBypass.mc.player.swingArm(EnumHand.MAIN_HAND);
                BurrowBypass.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BurrowBypass.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                BurrowBypass.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(old[0], old[1], false));
                return;
            }
        }
    }
    
    public void placeBlock(final BlockPos pos, final int slot) {
        if (slot == -1) {
            return;
        }
        final int prev = BurrowBypass.mc.player.inventory.currentItem;
        BurrowBypass.mc.player.inventory.currentItem = slot;
        BurrowBypass.mc.playerController.processRightClickBlock(BurrowBypass.mc.player, BurrowBypass.mc.world, this.blockData.position, this.blockData.face, new Vec3d((double)this.blockData.position.getX(), (double)this.blockData.position.getY(), (double)this.blockData.position.getZ()), EnumHand.MAIN_HAND);
        BurrowBypass.mc.player.swingArm(EnumHand.MAIN_HAND);
        BurrowBypass.mc.player.inventory.currentItem = prev;
    }
    
    public static int getHotbarSlot(final Block block) {
        for (int i = 0; i < 9; ++i) {
            final Item item = BurrowBypass.mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock)item).getBlock().equals(block)) {
                return i;
            }
        }
        return -1;
    }
    
    private Block getBlock() {
        if (getHotbarSlot((Block)Blocks.STONE_SLAB) != -1) {
            return (Block)Blocks.STONE_SLAB;
        }
        if (getHotbarSlot((Block)Blocks.STONE_SLAB2) != -1) {
            return (Block)Blocks.STONE_SLAB2;
        }
        return (Block)Blocks.WOODEN_SLAB;
    }
    
    public static Block getBlock(final int x, final int y, final int z) {
        return BurrowBypass.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }
    
    private int getBlockSlot() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = BurrowBypass.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                return i - 36;
            }
        }
        return -1;
    }
    
    private class BlockData
    {
        public BlockPos position;
        public EnumFacing face;
        
        public BlockData(final BlockPos position, final EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}
